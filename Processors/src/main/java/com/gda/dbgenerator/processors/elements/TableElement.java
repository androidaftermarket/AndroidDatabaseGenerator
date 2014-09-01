package com.gda.dbgenerator.processors.elements;

/**
 * Created by gdaAquino on 5/22/14.
 */
public class TableElement {

  private String className;

  private String packageName;

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getPath() {
    return packageName + "." + className;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TableElement{");
    sb.append("className='").append(className).append('\'');
    sb.append(", packageName='").append(packageName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
