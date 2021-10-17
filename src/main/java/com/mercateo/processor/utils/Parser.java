package com.mercateo.processor.utils;


import com.mercateo.processor.models.PackagingCandidate;

import java.util.List;

public interface Parser {

    List<String> getTestCases(String path);
    List<PackagingCandidate> extractPackagingCandidates(List<String> testCases);


}
