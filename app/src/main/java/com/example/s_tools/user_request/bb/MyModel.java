package com.example.s_tools.user_request.bb;

import java.util.List;

public class MyModel {
    List<Modeldata> files;

    public MyModel(List<Modeldata> files) {
        this.files=files;
    }

    public List<Modeldata> getFiles() {
        return files;
    }

    public void setFiles(List<Modeldata> files) {
        this.files=files;
    }
}
