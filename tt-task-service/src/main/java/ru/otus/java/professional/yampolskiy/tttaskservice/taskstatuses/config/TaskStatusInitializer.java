package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.services.TaskStatusService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(11)
@Slf4j
public class TaskStatusInitializer implements CommandLineRunner {

    private final TaskStatusService taskStatusService;

    @Override
    public void run(String... args) {
        log.info("🔧 Checking for default task statuses...");

        if (!taskStatusService.findAll().isEmpty()) {
            log.info("✅ Task statuses already exist. Skipping initialization.");
            return;
        }

        log.info("➕ Inserting default task statuses...");

        List<TaskStatus> defaults = List.of(
                new TaskStatus(null, "todo", "To Do", "Task is not yet started", false, true, 1, "#D3D3D3", null, null),
                new TaskStatus(null, "in_progress", "In Progress", "Work is ongoing", false, false, 2, "#87CEEB", null, null),
                new TaskStatus(null, "review", "Review", "Awaiting review", false, false, 3, "#FFD700", null, null),
                new TaskStatus(null, "done", "Done", "Task is completed", true, false, 4, "#32CD32", null, null),
                new TaskStatus(null, "cancelled", "Cancelled", "Task was cancelled", true, false, 5, "#A9A9A9", null, null)
        );

        defaults.forEach(taskStatusService::create);

        log.info("✅ Default task statuses initialized.");
    }
}