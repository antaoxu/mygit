package com.shrb.hop.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.shrb.hop.annotation.NotNull;
import com.shrb.hop.beans.ValidateResult;

public class ValidateUtil {

	public static <T> List<ValidateResult> validate(T t){
        List<ValidateResult> validateResults = new ArrayList<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field:fields) {
            if (field.isAnnotationPresent(NotNull.class)) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value==null) {
                    NotNull notNull = field.getAnnotation(NotNull.class);
                    ValidateResult validateResult = new ValidateResult();
                    validateResult.setMessage(notNull.message());
                    validateResults.add(validateResult);
                }
            }
            
        }
        
        return validateResults;
    }
}
