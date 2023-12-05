package uk.gov.hmcts.juror.support.generation.generators.code;

import lombok.Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;

@Data
public class GenerationClass {
    public static final String TAB = "\t";
    public static final String NEW_LINE = "\n";

    private final String name;
    private final String packageName;
    private final List<GenerationField> fields;
    private final List<GenerationMethod> methods;
    private final Set<String> imports;
    private final Map<String, String[]> interfaces;
    private Map.Entry<String, String[]> extend;

    public GenerationClass(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.imports = new HashSet<>();
        this.interfaces = new HashMap<>();
    }

    public void addImport(Class<?> clazz) {
        imports.add(clazz.getCanonicalName());
    }

    public void addField(GenerationField field, boolean addGetter, boolean addSetter) {
        fields.add(field);
        if (addGetter) {
            addMethod(new GenerationMethod(Visibility.PUBLIC,
                "get" + capitalise(field.name()),
                field.fieldType,
                "return " + field.name() + ";",
                null));
        }
        if (addSetter) {
            addMethod(new GenerationMethod(Visibility.PUBLIC,
                "set" + capitalise(field.name()),
                "void",
                "this." + field.name() + " = " + field.name() + ";",
                Map.of(field.fieldType, field.name)));
        }
    }

    public void addField(GenerationField field) {
        addField(field, false, false);
    }

    public void addMethod(GenerationMethod method) {
        methods.add(method);
    }

    public void build(ProcessingEnvironment processingEnv) throws IOException {
        try (PrintWriter writer =
                 new PrintWriter(processingEnv.getFiler().createSourceFile(packageName + "." + name).openWriter())) {
            writer.println("package " + packageName + ";");
            writer.println("");
            imports.forEach(importValue -> writer.println("import ".concat(importValue).concat(";")));
            writer.println("");
            writer.println("@SuppressWarnings(\"unchecked\")");
            writer.println("public class " + name);

            if (extend != null) {
                writer.print(" extends ");
                writer.print(extend.getKey());
                if (extend.getValue() != null && extend.getValue().length > 0) {
                    writer.print("<");
                    writer.print(String.join(", ", extend.getValue()));
                    writer.print(">");
                }
            }

            if (!interfaces.isEmpty()) {
                writer.print(" implements ");
                interfaces.forEach((key, value) -> {
                    writer.print(key);
                    if (value != null && value.length > 0) {
                        writer.print("<");
                        writer.print(String.join(", ", value));
                        writer.print(">");
                    }
                    //TODO add comma if not last
                });
            }
            writer.print("{");
            fields.forEach(field -> writer.println(field.getCode()));
            writer.println("");
            methods.forEach(method -> writer.println(method.getCode()));
            writer.println("}");
        }
    }

    public void addInterface(String interfaceClass, String... genericTypes) {
        interfaces.put(interfaceClass, genericTypes);
    }

    public void addExtends(String extendsClass, String... genericTypes) {
        this.extend = new HashMap.SimpleEntry<>(extendsClass, genericTypes);
    }


    public record GenerationField(Visibility visibility, String name, String fieldType, String initializationString) {
        public String getCode() {
            return TAB + visibility.name().toLowerCase(Locale.getDefault()) + " " + fieldType + " " + name
                + (initializationString == null ? "" : " = " + initializationString) + ";";
        }
    }


    public record GenerationMethod(Visibility visibility, String name,
                                   String type, String content,
                                   Map<String, String> parameters) {
        public String getCode() {
            return TAB + visibility.name().toLowerCase(Locale.getDefault()) + " " + type + " " + name
                + getParametersCode()
                + " {"
                + NEW_LINE
                + getContentWithTabbing()
                + NEW_LINE + TAB
                + "}";
        }

        private String getParametersCode() {
            if (parameters == null || parameters.isEmpty()) {
                return "()";
            }
            return "("
                + parameters.entrySet().stream().map(entry -> entry.getKey() + " " + entry.getValue())
                .reduce((a, b) -> a + ", " + b).get()
                + ")";
        }

        private String getContentWithTabbing() {
            return TAB.repeat(2) + content.replace(NEW_LINE, NEW_LINE + TAB.repeat(2));
        }
    }

    public enum Visibility {
        PUBLIC, PRIVATE, PROTECTED, PACKAGE_PRIVATE;
    }

    public static String capitalise(String text) {
        return text.substring(0, 1).toUpperCase(Locale.getDefault()) + text.substring(1);
    }
}
