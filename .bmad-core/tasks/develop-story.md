# Task: Develop Story

This task defines the core workflow for a Developer agent to implement a user story, referencing a Definition of Done checklist.

## 1. Setup
- Input: `{story_file_path}`
- Action: Read the story file and confirm its status is "Ready for Dev".

## 2. Iterative Development Cycle
- Action: For each `[ ] Task` in the story file, perform the following:

  ### a. Implement & Test
  - Action: Write source code and corresponding unit/integration tests.

  ### b. Validate
  - Action: Run all new and relevant regression tests until they pass.

  ### c. Update Story
  - Action: Mark the task as `[x]` and update the "File List" in the story's Dev Agent Record.

## 3. Finalization
- Action: Once all tasks are `[x]`, run a full application regression test.
- Action: Execute the Definition of Done checklist by calling the `execute-checklist` task with `story-dod-checklist.md` as input.

## 4. Handoff
- Action: If all final checks pass, update the story `Status` to "Ready for Review".
- Output: "Story {story_file_path} is complete and ready for QA review."
