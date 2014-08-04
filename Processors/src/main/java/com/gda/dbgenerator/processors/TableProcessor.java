package com.gda.dbgenerator.processors;

import com.gda.dbgenerator.annotations.DbConfig;
import com.gda.dbgenerator.annotations.Field;
import com.gda.dbgenerator.annotations.Table;
import com.gda.dbgenerator.processors.elements.FieldElement;
import com.gda.dbgenerator.processors.elements.TableElement;
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

@SupportedAnnotationTypes("com.gda.dbgenerator.annotations.DbConfig")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TableProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

       log("Finding Config Class");

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DbConfig.class);
        Iterator<? extends Element> iterator = elements.iterator();

        boolean useContentProvider;
        String databasePackage;
        String databaseName;
        String authority;
        int databaseVersion;

        if (elements.size() == 1) {
            Element element = iterator.next();
            authority = element.getAnnotation(DbConfig.class).authority();
            databaseName = element.getAnnotation(DbConfig.class).databaseName();
            databaseVersion = element.getAnnotation(DbConfig.class).databaseVersion();
            //useContentProvider = element.getAnnotation(DbConfig.class).useContentProvider();
            useContentProvider = false; //ContentProvider Not Supported TODO: Support ContentProvider
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();
            databasePackage = packageElement.getQualifiedName().toString();
            log("DatabaseName : " + databaseName);
            log("DatabaseVersion : " + databaseVersion);
        } else {
            log("DbConfig Error. Need only ONE DbConfig.");
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

            List<FieldElement> tableClassFieldElement = getFieldElementFields(fieldsOfClassElement);

            generateDbTable(tableClassElement.getPackageName(), tableClassElement.getClassName(), databaseName, useContentProvider, tableClassFieldElement);

            if (useContentProvider == false) {
                generateDbTableDAO(databasePackage, tableClassElement.getPackageName(), tableClassElement.getClassName(), databaseName, tableClassFieldElement);
            }
        }

        generateDbOpenHelper(databasePackage, databaseName, databaseVersion, tableElementList);
        generateDAOListener(databasePackage);
        generateMainThreadExecutor(databasePackage);
        generateBackgroundThreadExecutor(databasePackage);

        if (useContentProvider) generateDbProvider(databasePackage, databaseName, authority, tableElementList);

        return true;
    }

    private void generateDAOListener(String packageName) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        Template template = ve.getTemplate("DAOListener.vm");

        try {
            generateSourceCode(template, context, packageName + ".DAOListener");
        } catch (IOException e) {
            logE(e.getMessage());
        }
    }

    private void generateMainThreadExecutor(String packageName) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        Template template = ve.getTemplate("MainThreadExecutor.vm");

        try {
            generateSourceCode(template, context, packageName + ".MainThreadExecutor");
        } catch (IOException e) {
            logE(e.getMessage());
        }
    }

    private void generateBackgroundThreadExecutor(String packageName) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("packageName", packageName);
        Template template = ve.getTemplate("BackgroundThreadExecutor.vm");

        try {
            generateSourceCode(template, context, packageName + ".BackgroundThreadExecutor");
        } catch (IOException e) {
            logE(e.getMessage());
        }
    }

    private void generateDbTableDAO(String databasePackageName, String packageName, String className, String databaseName, List<FieldElement> fields) {
        VelocityEngine ve = new VelocityEngine(getVelocityProperty());
        ve.init();

        VelocityContext context = new VelocityContext();
        context.put("display", new DisplayTool());
        context.put("className", className);
        context.put("packageName", packageName);
        context.put("databaseName", databaseName);
        context.put("databasePackageName", databasePackageName);
        context.put("fields", fields);
        Template template = ve.getTemplate("DAO.vm");

        try {
            generateSourceCode(template, context, packageName + "." + className + "DAO");
        } catch (IOException e) {
            logE(e.getMessage());
        }
    }
    private void generateDbTable(String packageName, String className, String databaseName, boolean useContentProvider, List<FieldElement> fields) {
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
            logE(e.getMessage());
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
            logE(e.getMessage());
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
            logE(e.getMessage());
        }
    }

    private void log(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

    private void logE(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

    private TableElement getTableElement(Element element) {

        TypeElement classElement      = (TypeElement) element;
        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

        String className   = classElement.getSimpleName().toString();
        String packageName = packageElement.getQualifiedName().toString();

        TableElement tableElement = new TableElement();
        tableElement.setClassName(className);
        tableElement.setPackageName(packageName);

        log("/n" + tableElement.toString());

        return tableElement;
    }

    private List<FieldElement> getFieldElementFields(List<? extends Element> fieldsOfClassElement) {

        List<FieldElement> tableClassFieldElement = new ArrayList<FieldElement>();

        for (Element field : fieldsOfClassElement) {

            if (field.getAnnotation(Field.class) == null) continue;

            VariableElement variableElement = (VariableElement) field;

            FieldElement fieldElement = new FieldElement();
            fieldElement.setFieldName(variableElement.getSimpleName().toString());
            fieldElement.setFieldType(variableElement.getAnnotation(Field.class).type().getValue());
            fieldElement.setUnique(variableElement.getAnnotation(Field.class).unique());
            fieldElement.setNotNull(variableElement.getAnnotation(Field.class).notNull());
            tableClassFieldElement.add(fieldElement);
            log("/n" + fieldElement.toString());
        }

        return tableClassFieldElement;
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
