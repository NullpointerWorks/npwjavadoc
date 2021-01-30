package com.nullpointerworks.xmlmaker.examples;

import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE, MODULE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExampleAnnotation 
{
	String[] value();
}
