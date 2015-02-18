package org.flowdev.asciidoctorplugin;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.extension.RubyExtensionRegistry;
import org.asciidoctor.extension.spi.ExtensionRegistry;

import java.io.InputStream;

public class FlowparserExtension implements ExtensionRegistry {
    @Override
    public void register(Asciidoctor asciidoctor) {
        System.err.println("Registering extension: flowdev");
        RubyExtensionRegistry rubyExtensionRegistry = asciidoctor
                .rubyExtensionRegistry();
        InputStream diagramRbInputStream = this.getClass().getResourceAsStream(
                "/gems/asciidoctor-diagram-1.2.1-java/lib/asciidoctor-diagram.rb");
        rubyExtensionRegistry.loadClass(diagramRbInputStream);
        // rubyExtensionRegistry.block("diagram", "asciidoctor-diagram");

        JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();

        javaExtensionRegistry.block("flowdev", new FlowparserBlockProcessor("flowdev", asciidoctor));
        System.err.println("Registered extension: flowdev");
    }
}
