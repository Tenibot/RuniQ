package fmi.codes.project.quiz;

import fmi.codes.project.location.Location;

public record Task(Integer taskId, String taskQuestion, String taskAnswer, String taskClue, Location taskLocation) {
}
