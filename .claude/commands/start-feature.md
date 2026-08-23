Start implementation work on a feature, following project git conventions in CLAUDE.md.

Arguments: $ARGUMENTS — expected as `<jira-ticket-or-na> <short-desc>`
Example: `/start-feature PROJ-1234 fix-memory-leak`
Example (no ticket): `/start-feature na fix-memory-leak`

Steps:
1. Parse the Jira ticket ID and short description from $ARGUMENTS.
   - If the ticket ID isn't `na`, fetch ticket details (via Jira MCP if available) for context.
2. Enter plan mode: read the relevant codebase, propose an implementation approach, and confirm with the user before writing code.
3. Create a branch named `username_jiraticket_shortdesc` per CLAUDE.md (ask for username if not already known).
4. Implement the change in small, logical commits, each with a concise message ending in the Jira ticket ID (or `N/A` if none).
5. Never commit to `master` or `develop`. Never push directly to protected branches.
6. When work is ready, push the branch and open a PR with a clear description (summary, why, testing done, linked ticket).
7. Do NOT merge the PR — stop after opening it and hand back to the user for review.
