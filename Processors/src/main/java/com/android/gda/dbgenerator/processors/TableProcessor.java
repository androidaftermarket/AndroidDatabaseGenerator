package com.android.gda.dbgenerator.processors;

import com.android.gda.dbgenerator.annotations.Column;
import com.android.gda.dbgenerator.annotations.DbConfig;
import com.android.gda.dbgenerator.annotations.Table;
import com.android.gda.dbgenerator.processors.elements.ColumnElement;
import com.android.gda.dbgenerator.processors.elements.TableElement;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DisplayTool;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.*;

/**
 * Created by gdaAquino on 5/22/14.
 */

@SupportedAnnotationTypes("com.android.gda.dbgenerator.annotations.DbConfig")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TableProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

       log("Finding Config Class");

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DbConfig.class);
        Iterator<? extends Element> iterator = elements.iterator();

        boolean useContentProvider = false;
        boolean withConfig = false;
        String databasePackage = null;
        String databaseName = null;
        String authority = null;
        int databaseVersion = 0;

        if (elements.size() == 1) {
            withConfig = true;
            Element element = iterator.next();
            authority = element.getAnnotation(DbConfig.class).authority();
            databaseName = element.getAnnotation(DbConfig.class).databaseName();
            databaseVersion = element.getAnnotation(DbConfig.class).databaseVersion();
            useContentProvider = element.getAnnotation(DbConfig.class).useContentProvider();
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();
            databasePackage = packageElement.getQualifiedName().toString();
            log("DatabaseName : " + databaseName);
            log("DatabaseVersion : " + databaseVersion);
        } else {
            log("DB Config File can only be ONE.");
            return true;
        }

        List<TableElement> tableElementList = new ArrayList<TableElement>();
        elements = roundEnv.getElementsAnnotatedWith(Table.class);
        iterator = elements.iterator();

        log("Starting Android Annotation Processor");
        log("Found Element With Table.class Annotation " + elements.size());

        while (iterator.hasNext()) {

            Element element = iterator.next();

            if (element.getKind() != ElementKind.CLASS) {
                log("Element " + element.getKind().toString() + " is not = ElementKind.CLASS");
                continue;
            }

            TableElement tableClassElement = getTableElement(element);
            tableElementList.add(tableClassElement);

            List<? extends Element> fieldsOfClassElement = ElementFilter.fieldsIn(element.getEnclosedElements());

            List<ColumnElement> tableClassColumnElement = getColumnElementFields(fieldsOfClassElement);

            generateDbTable(tableClassElement.getPackageName(), tableClassElement.getClassName(), databaseName, useContentProvider, tableClassColumnElement);

            if (useContentProvider == false && withConfig) {
                generateDbTableDAO(tableClassElement.getPackageName(), tableClassElement.getClassName(), databaseName, tableClassColumnElement);
            }
        }

        if (withConfig) {
            generateDbOpenHelper(databasePackage, databaseName, databaseVersion, tableElementList);
            if (useContentProvider) generateDbProvider(databasePackage, databaseName, authority, tableElementList);
        }

        return true;
    }

    private void generateDbTableDAO(String packageName, String className, String databaseName, List<ColumnElement> fields) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("display", new DisplayTool());
        context.put("className", className);
        context.put("packageName", packageName);
        context.put("databaseName", databaseName);
        context.put("fields", fields);
        Template template = ve.getTemplate("DAO.vm");

        try {
            generateSourceCode(template, context, packageName + "." + className + "DAO");
        } catch (IOException e) {
            log("Failed to generate Source Code");
            log(e.getMessage());
        }
    }
    private void generateDbTable(String packageName, String className, String databaseName, boolean useContentProvider, List<ColumnElement> fields) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("display", new DisplayTool());
        context.put("className", className);
        context.put("packageName", packageName);
        context.put("databaseName", databaseName);
        context.put("useContentProvider", useContentProvider);
        context.put("fields", fields);
        Template template = ve.getTemplate("Table.vm");

        try {
            generateSourceCode(template, context, packageName + "." + className + "Table");
        } catch (IOException e) {
            log("Failed to generate Source Code");
            log(e.getMessage());
        }
    }


    private void generateDbOpenHelper(String packageName, String databaseName, int databaseVersion, List<TableElement> tables) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("display", new DisplayTool());
        context.put("packageName", packageName);
        context.put("databaseName", databaseName);
        context.put("databaseVersion", databaseVersion);
        context.put("tables", tables);
        Template template = ve.getTemplate("SQLiteOpenHelper.vm");

        try {
            generateSourceCode(template, context, packageName + "." + databaseName + "OpenHelper");
        } catch (IOException e) {
            log(e.getMessage());
        }
    }

    private void generateDbProvider(String packageName, String databaseName, String authority, List<TableElement> tables) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("display", new DisplayTool());
        context.put("packageName", packageName);
        context.put("databaseName", databaseName);
        context.put("authority", authority);
        context.put("tables", tables);
        Template template = ve.getTemplate("ContentProvider.vm");

        try {
            generateSourceCode(template, context, packageName + "." + databaseName + "Provider");
        } catch (IOException e) {
            log(e.getMessage());
        }
    }

    private void log(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private TableElement getTableElement(Element element) {

        TypeElement classElement      = (TypeElement) element;
        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

        String className   = classElement.getSimpleName().toString();
        String packageName = packageElement.getQualifiedName().toString();

        TableElement tableElement = new TableElement();
        tableElement.setClassName(className);
        tableElement.setPackageName(packageName);
        log(tableElement.toString());

        return tableElement;
    }

    private List<ColumnElement> getColumnElementFields(List<? extends Element> fieldsOfClassElement) {

        List<ColumnElement> tableClassColumnElement = new ArrayList<ColumnElement>();

        for (Element field : fieldsOfClassElement) {

            if (field.getAnnotation(Column.class) == null) continue;

            VariableElement variableElement = (VariableElement) field;

            ColumnElement columnElement = new ColumnElement();
            columnElement.setColumnName(variableElement.getSimpleName().toString());
            columnElement.setColumnType(variableElement.getAnnotation(Column.class).type().getValue());
            columnElement.setUnique(variableElement.getAnnotation(Column.class).unique());
            columnElement.setNotNull(variableElement.getAnnotation(Column.class).notNull());
            tableClassColumnElement.add(columnElement);
        }
        log(tableClassColumnElement.toString());
        return tableClassColumnElement;
    }

    private void generateSourceCode(Template template, VelocityContext context, String path) throws IOException {
        log("Starting Generating Source Code for ... " + path);
        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(path);
        Writer writer = jfo.openWriter();
        template.merge(context, writer);
        writer.close();
        log("Finished Generating Source Code for... "  + path);
    }

    private Properties getVelocityProperty() {
        Properties props = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");

        try {
            props.load(url.openStream());
        } catch (IOException e) {
            log("Failed to Load Velocity Properties : velocity.properties");
            log(e.getMessage());
        }

        return props;
    }
}
