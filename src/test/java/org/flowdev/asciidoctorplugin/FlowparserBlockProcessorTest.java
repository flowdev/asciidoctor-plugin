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
                "flowdev block!\n" +
                "\n" +
                "Blue blue\n";

        String expectedHtml = "<div class=\"paragraph\">\n" +
                "<p>Writing AsciiDoc is <em>easy</em>!</p>\n" +
                "</div>";
        String html = asciidoctor.convert(inputAdoc, options);
        Assert.assertEquals("The wrong asciidoc is generated!", expectedHtml, html);
    }
}
