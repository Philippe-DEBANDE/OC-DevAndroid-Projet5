package com.cleanup.todoc.repositories;

import android.arch.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) {this.projectDao = projectDao; }

    // Get Project
    public LiveData<Project> getProject(long projectId) {return  this.projectDao.getProject(projectId); }

    // --- GET ALL PROJECTS ---
    public LiveData<List<Project>> getProjects() { return this.projectDao.getProjects(); }
}

