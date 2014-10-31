package org.flowdev.asciidoctorplugin;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class FlowparserBlockProcessor extends BlockProcessor {

    public FlowparserBlockProcessor(String name, Map<String, Object> config) {
        super(name, config);
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        List<String> lines = reader.readLines();
        StringBuilder sb = new StringBuilder(4096);
        sb.append("flowdev BlockProcessor: \n");
        for (String line : lines) {
            sb.append(line).append('\n');
        }
        return createBlock(parent, "paragraph", asList(sb.toString()), attributes, new HashMap<>());
    }
}
