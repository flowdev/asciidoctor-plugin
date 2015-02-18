package org.flowdev.asciidoctorplugin;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Preprocessor;
import org.asciidoctor.extension.PreprocessorReader;

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
                    sbDoc.append("\n[graphviz, ").append(flowName).append(", svg]\n....\n");
//                    sbDoc.append(sbFlow);
                    sbDoc.append("digraph Mini {\n" +
                            "  // rankdir=LR;\n" +
                            "  node [shape=Mrecord,style=filled,fillcolor=\"#00CC00\",rank=same];\n" +
                            "\n" +
                            "  doIt [label=\"doIt\\n(DoIt)|{ <in> in| }\"] ;\n" +
                            "\n" +
                            "  node [shape=plaintext,style=plain,rank=same];\n" +
                            "\n" +
                            "  \"in\" -> doIt:in ;\n" +
                            "}\n");
                    sbDoc.append("....\n\n");
                    sbFlow.insert(0, "flow " + flowName + " {\n");
                    sbFlow.append("}\n");
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
                    System.err.print("FlowDev block found: " + flowName);
                    inFlowdev = true;
                } else {
                    sbDoc.append(line).append("\n");
                }
            }
        }
        return sbDoc.toString();
    }
}
