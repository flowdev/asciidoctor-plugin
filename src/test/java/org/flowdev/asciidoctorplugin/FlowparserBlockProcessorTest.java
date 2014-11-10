package org.flowdev.asciidoctorplugin;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class FlowparserBlockProcessorTest {
    private Asciidoctor asciidoctor;
    private JavaExtensionRegistry extensionRegistry;
    private OptionsBuilder options;

    @Before
    public void setUp() {
        asciidoctor = Asciidoctor.Factory.create();
        extensionRegistry = asciidoctor.javaExtensionRegistry();
        extensionRegistry.block("flowdev", FlowparserBlockProcessor.class);

        AttributesBuilder attributes = AttributesBuilder.attributes().backend("html5");
        options = OptionsBuilder.options().attributes(attributes);
    }

    @Test
    public void testProcessor() {
        String inputAdoc = "Bla bla\n" +
                "\n" +
                "[flowdev]\n" +
                "....\n" +
                "flow NumberedPorts {\n" +
                "    -> doIt() -> doThis() -> doThat() -> ;\n" +
                "\n" +
                "\n" +
                "    -> subIn.0 doIt() subOut.0 -> subIn.0 doThis() subOut.0 -> subIn.0 doThat() subOut.0 -> ;\n" +
                "\n" +
                "    -> subIn.1 doIt() subOut.1 -> subIn.1 doThis() subOut.1 -> subIn.1 doThat() subOut.1 -> ;\n" +
                "\n" +
                "    -> subIn.2 doIt() subOut.2 -> subIn.2 doThis() subOut.2 -> subIn.2 doThat() subOut.2 -> ;\n" +
                "}\n" +
                "....\n" +
                "\n" +
                "Blue blue\n";

        String expectedHtml = "<div class=\"paragraph\">\n" +
                "<p>Bla bla</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>flowdev BlockProcessor: flowdev block!</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Blue blue</p>\n" +
                "</div>";
        String html = asciidoctor.convert(inputAdoc, options);
        Assert.assertEquals("The wrong asciidoc is generated!", expectedHtml, html);
    }
}
