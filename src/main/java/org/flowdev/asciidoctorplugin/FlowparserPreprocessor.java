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
    public static final String DEFAULT_FLOW_NAME = "FlowDiagram";
    public static final String DEFAULT_ORIENTATION = "vertical";

    public FlowparserPreprocessor(Map<String, Object> config) {
        super(config);
    }

    @Override
    public PreprocessorReader process(Document document, PreprocessorReader reader) {
        int lineno = reader.getLineno();
		Map<String, Object> attributes = document.getAttributes();
		// The casts are necessary because the strings can be null:
        reader.push_include(readDocument(reader), (String) attributes.get("docfile"), (String) attributes.get("docdir"), lineno, attributes);
        return reader;
    }

    private static String readDocument(PreprocessorReader reader) {
        StringBuilder sbDoc = new StringBuilder(8192);
        StringBuilder sbFlow = new StringBuilder(2048);
        boolean inFlowdev = false;
        boolean inLiteral = false;
        String flowName = DEFAULT_FLOW_NAME;
        String orientation = DEFAULT_ORIENTATION;
        Pattern p = Pattern.compile("\\[flowdev\\s*(,\\s*(\\w+)\\s*(,\\s*(\\w+)\\s*)?)?\\s*\\]\\s*");

        List<String> lines = reader.readLines();
        for (String line : lines) {
            if (line.startsWith("....")) {
                if (inFlowdev && inLiteral) {
                    inFlowdev = false;
                    inLiteral = false;
                    sbFlow.insert(0, "version 0.1\nflow " + flowName + " {\n");
                    sbFlow.append("}\n");
                    sbDoc.append(PluginMain.compileFlowToAdoc(sbFlow.toString(), orientation.substring(0, 1).equalsIgnoreCase("h")));
                    sbFlow = new StringBuilder(2048);
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
                    flowName = m.group(2) == null ? DEFAULT_FLOW_NAME : m.group(2);
                    orientation = m.group(4) == null ? DEFAULT_ORIENTATION : m.group(4);
                    inFlowdev = true;
                } else {
                    sbDoc.append(line).append("\n");
                }
            }
        }
        return sbDoc.toString();
    }
}
