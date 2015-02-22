package org.flowdev.asciidoctorplugin;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.extension.RubyExtensionRegistry;
import org.asciidoctor.extension.spi.ExtensionRegistry;
import org.asciidoctor.internal.JRubyRuntimeContext;
import org.asciidoctor.internal.RubyUtils;

import java.io.InputStream;

public class FlowparserExtension implements ExtensionRegistry {
    @Override
    public void register(Asciidoctor asciidoctor) {
//        RubyExtensionRegistry rubyExtensionRegistry = asciidoctor
//                .rubyExtensionRegistry();
//        InputStream diagramRbInputStream = this.getClass().getResourceAsStream(
//                "/gems/asciidoctor-diagram-1.2.1-java/lib/asciidoctor-diagram.rb");
//        rubyExtensionRegistry.loadClass(diagramRbInputStream);
//        rubyExtensionRegistry.block("diagram", "asciidoctor-diagram");

//        RubyUtils.requireLibrary(JRubyRuntimeContext.get(), "asciidoctor-diagram");

        JavaExtensionRegistry javaExtensionRegistry = asciidoctor.javaExtensionRegistry();
        javaExtensionRegistry.preprocessor(FlowparserPreprocessor.class);
//        javaExtensionRegistry.block("flowdev", new FlowparserBlockProcessor("flowdev", asciidoctor));
    }
}
