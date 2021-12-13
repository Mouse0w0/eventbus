package com.github.mouse0w0.eventbus.ap;


import com.github.mouse0w0.eventbus.Event;
import com.github.mouse0w0.eventbus.Listener;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ListenerProcessor extends AbstractProcessor {

    private static final String CLASS_NAME = Listener.class.getName();

    private TypeMirror eventTypeMirror;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(CLASS_NAME);
        return set;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        eventTypeMirror = processingEnv.getElementUtils().getTypeElement(Event.class.getName()).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getElementsAnnotatedWith(Listener.class)) {
                ExecutableElement method = (ExecutableElement) element;

                List<? extends VariableElement> parameters = method.getParameters();

                if (parameters.size() != 1) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "The count of listener method parameter must be 1.", method);
                }

                VariableElement event = parameters.get(0);

                if (!processingEnv.getTypeUtils().isAssignable(event.asType(), eventTypeMirror)) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "The type of parameter of listener method must be Event or its sub class.", method);
                }

                if (!hasModifier(method, Modifier.PUBLIC)) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Listener method must be public", method);
                }

                if (method.getReturnType().getKind() != TypeKind.VOID) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "The return type of listener method must be void.", method);
                }
            }
        }
        return false;
    }

    private static boolean hasModifier(Element element, Modifier modifier) {
        for (Modifier m : element.getModifiers()) {
            if (modifier == m) return true;
        }
        return false;
    }
}
