package org.flowdev.asciidoctorplugin;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Preprocessor;
import org.asciidoctor.extension.PreprocessorReader;
import org.flowdev.flowparser.PluginMain;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlowparserPreprocessor extends Preprocessor {

    public FlowparserPreprocessor(Map<String, Object> config) {
        super(config);
        System.err.println("FlowparserTreeprocessor: constructor called, config: " + config);
    }

    @Override
    public PreprocessorReader process(Document document, PreprocessorReader reader) {
        int lineno = reader.getLineno();
//        System.err.println("Prepoc attrs: " + document.getAttributes());
        reader.push_include(readDocument(reader), document.getAttributes().get("docfile").toString(), document.getAttributes().get("docdir").toString(), lineno, document.getAttributes());
        return reader;
    }

    private static String readDocument(PreprocessorReader reader) {
        StringBuilder sbDoc = new StringBuilder(8192);
        StringBuilder sbFlow = new StringBuilder(2048);
        boolean inFlowdev = false;
        boolean inLiteral = false;
        String flowName = "FlowDiagram";
        Pattern p = Pattern.compile("\\[flowdev\\s*[,]?\\s*(\\w+)?\\s*\\]\\s*");

        List<String> lines = reader.readLines();
        for (String line : lines) {
            if (line.startsWith("....")) {
                if (inFlowdev && inLiteral) {
                    inFlowdev = false;
                    inLiteral = false;
                    sbFlow.insert(0, "version 0.1\nflow " + flowName + " {\n");
                    sbFlow.append("}\n");
                    sbDoc.append(PluginMain.compileFlowToAdoc(sbFlow.toString()));
                    System.err.print("Appending FlowDev block: " + sbFlow);
                    sbFlow = new StringBuilder(2048);
                    System.err.println("FlowDev block ended!");
                } else if (inFlowdev) {
                    inLiteral = true;
                } else {
                    sbDoc.append(line).append("\n");
                }
            } else if (inFlowdev && inLiteral) {
                sbFlow.append(line).append("\n");
            } else {
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    flowName = m.group(1) == null ? "FlowDiagram" : m.group(1);
                    System.err.println("FlowDev block found: " + flowName);
                    inFlowdev = true;
                } else {
                    sbDoc.append(line).append("\n");
                }
            }
        }
        return sbDoc.toString();
    }
}
