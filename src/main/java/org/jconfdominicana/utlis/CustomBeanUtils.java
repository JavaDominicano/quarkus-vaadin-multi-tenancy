package org.jconfdominicana.utlis;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class CustomBeanUtils {

    public static void copyNonNullProperties(Object source, Object target) throws Exception {
        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        java.beans.PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(source);

        for (java.beans.PropertyDescriptor descriptor : descriptors) {
            String propertyName = descriptor.getName();
            Object value = propertyUtilsBean.getNestedProperty(source, propertyName);

            if (value != null) {
                propertyUtilsBean.setNestedProperty(target, propertyName, value);
            }
        }
    }
}
