package com.library.service;

import com.library.dao.IssueDAO;
import com.library.entity.Issue;

import java.util.List;

public class IssueService {

    private final IssueDAO issueDAO;

    public IssueService() {
        this.issueDAO = new IssueDAO();
    }

    public void issueBook(Issue issue) {
        issueDAO.saveIssue(issue);
    }

    public List<Issue> getActiveIssuesByUser(Long userId) {
        return issueDAO.getActiveIssuesByUser(userId);
    }


    public List<Issue> listIssues() {
        return issueDAO.getAllIssues();
    }

    public void deleteIssue(Long id) {
        issueDAO.deleteIssue(id);
    }

    public List<Issue> getAllByBookId(Long bookId) {
        return issueDAO.getAllByBookId(bookId);
    }

    public void deleteIssuesByUserId(Long userId) {
        issueDAO.deleteIssuesByUserId(userId);
    }
}
