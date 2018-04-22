package io.lsn.dailydocker.managedbeans;

import io.lsn.dailydocker.domain.ScoresParser;
import io.lsn.dailydocker.service.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class PresentationBean {

    @Autowired
    ScoresParser parser;

    @Autowired
    ScoresService service;




}
