package org.flowdev.asciidoctorplugin;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

import java.util.*;

public class FlowparserBlockProcessor extends BlockProcessor {
    private static Map<String, Object> configs = new HashMap<>();

    static {
        configs.put("contexts", Arrays.asList(":literal"));
        configs.put("content_model", ":raw");
//        configs.put("content_model", ":verbatim");
    }

    private final Asciidoctor asciidoctor;

    public FlowparserBlockProcessor(String name, Asciidoctor asciidoctor) {
        super(name, configs);
        this.asciidoctor = asciidoctor;
        System.err.println("FlowparserBlockProcessor: constructor called");
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        System.err.println("FlowparserBlockProcessor: process called");
        System.err.println("FlowparserBlockProcessor: attributes: " + attributes);
        System.err.println("FlowparserBlockProcessor: Flowname: " + getFlowName(attributes.get(2)));
        String flowName = getFlowName(attributes.get(2));
        List<String> lines = reader.readLines();
        StringBuilder sbFlow = new StringBuilder(4096);
//        for (String line : lines) {
//            System.out.println(line);
//            sbFlow.append(line).append('\n');
//        }
        // TODO: call flow compiler!

        StringBuilder sbDiag = new StringBuilder(4096);
//        sbDiag.append("\n[graphviz, FlowdevDiagram, svg]\n....\n");
        sbDiag.append("digraph Mini {\n" +
                "  // rankdir=LR;\n" +
                "  node [shape=Mrecord,style=filled,fillcolor=\"#00CC00\",rank=same];\n" +
                "\n" +
                "  doIt [label=\"doIt\\n(DoIt)|{ <in> in| }\"] ;\n" +
                "\n" +
                "  node [shape=plaintext,style=plain,rank=same];\n" +
                "\n" +
                "  \"in\" -> doIt:in ;\n" +
                "}\n");
//        sbDiag.append("....\n\n");
//        System.err.println("FlowparserBlockProcessor: calling sub-asciidoctor");
//        String renderedDiagram = asciidoctor.convert(sbFlow.toString(), new HashMap<>());
//        System.err.println("FlowparserBlockProcessor: converted diagram.length: " + renderedDiagram.length());
//        renderedDiagram = asciidoctor.render(sbFlow.toString(), new HashMap<>());
//        System.err.println("FlowparserBlockProcessor: rendered diagram.length: " + renderedDiagram.length());

        System.err.println("FlowparserBlockProcessor: creating block");
        Map subAttrs = new HashMap<>();
        subAttrs.put(1, "diagram");
        subAttrs.put(2, flowName);
        subAttrs.put(3, "svg");
        System.err.println("FlowparserBlockProcessor: subAttrs: " + subAttrs);
        return createBlock(parent, "literal", sbDiag.toString(), (Map<String, Object>) subAttrs, new HashMap<>());
//        return null;
    }

    private static String getFlowName(Object value) {
        return value == null ? "FlowdevDiagram" : value.toString();
    }
}
