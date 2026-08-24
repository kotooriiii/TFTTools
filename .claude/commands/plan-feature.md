Discuss and draft a feature with the user. This is a PLANNING-ONLY step — do not write or modify any code, and do not create a branch.

Arguments: $ARGUMENTS (optional — a rough feature description or existing Jira ticket ID)

Steps:
1. If $ARGUMENTS looks like a Jira ticket ID, fetch its details (via Jira MCP if available) and use it as context. Otherwise, treat $ARGUMENTS as a rough feature idea.
2. Ask clarifying questions as needed — scope, affected services, edge cases, non-goals.
3. Read the relevant parts of the codebase to ground the discussion in how things actually work today.
4. Propose an approach: summary, affected files/services, tradeoffs, open questions, rough sequencing.
5. Once the user is aligned on the approach, draft a Jira ticket (title + description + acceptance criteria) for them to review. If all looks great, ask if you should file it on their behalf. 
6. Do NOT create a branch or write code in this command. When the user is ready to build, tell them to run `/start-feature <ticket-id-or-na> <short-desc>`.
