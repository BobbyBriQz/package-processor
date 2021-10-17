package com.mercateo.processor;

import com.mercateo.processor.services.DPPackageProcessor;
import com.mercateo.processor.services.PackageProcessor;
import com.mercateo.processor.utils.Parser;
import com.mercateo.processor.utils.SimpleParser;
import com.mercateo.processor.utils.Validator;

public class PackageProcessorApplication {

    public static void main(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException("Please specify a path");

        Validator validator = new Validator();
        Parser parser = new SimpleParser();
        PackageProcessor processor = new DPPackageProcessor(parser,validator);
        processor.run(args[0]);

    }
}
