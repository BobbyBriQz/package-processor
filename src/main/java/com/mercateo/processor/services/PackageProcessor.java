package com.mercateo.processor.services;

import com.mercateo.processor.models.PackagingCandidate;
import com.mercateo.processor.models.ProcessedPackage;
import com.mercateo.processor.utils.Parser;
import com.mercateo.processor.utils.Validator;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PackageProcessor {

    public Validator validator;
    public Parser parser;

    public List<ProcessedPackage> processCandidates(List<PackagingCandidate> candidates){
        return candidates.stream()
                .map(this::process)
                .collect(Collectors.toList());
    }

    public abstract void run(String path);
    abstract void display(List<ProcessedPackage> packages);
    abstract ProcessedPackage process(PackagingCandidate candidate);

}
