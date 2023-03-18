package fmi.codes.project.task;

import fmi.codes.project.location.Location;

public record Task(String taskQuestion, String taskAnswer, String taskClue, Location taskLocation) {
}
