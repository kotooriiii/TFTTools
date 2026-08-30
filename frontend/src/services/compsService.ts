export interface PlacementDTO {
    unitApiName: string;
    row: number;
    col: number;
}

export interface CompResponse {
    id: string;
    placements: PlacementDTO[];
    createdAt: string;
}

export class CompsApiError extends Error {}

const parseErrorBody = async (response: Response): Promise<CompsApiError> => {
    const body = await response.json().catch(() => null);

    if (body?.error) {
        return new CompsApiError(body.error);
    }

    return new CompsApiError(`Request failed: ${response.status}`);
};

export const compsService = {
    async saveComp(token: string, placements: PlacementDTO[]): Promise<CompResponse> {
        const response = await fetch('http://localhost:8080/comps', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({ placements })
        });

        if (!response.ok) {
            throw await parseErrorBody(response);
        }

        return await response.json();
    },

    async listComps(token: string): Promise<CompResponse[]> {
        const response = await fetch('http://localhost:8080/comps', {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!response.ok) {
            throw await parseErrorBody(response);
        }

        return await response.json();
    },

    async deleteComp(token: string, id: string): Promise<void> {
        const response = await fetch(`http://localhost:8080/comps/${id}`, {
            method: 'DELETE',
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!response.ok) {
            throw await parseErrorBody(response);
        }
    },

    async getTeamCode(unitApiNames: string[]): Promise<string> {
        const response = await fetch('http://localhost:8080/tools/team-code', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ unitApiNames })
        });

        if (!response.ok) {
            throw new CompsApiError(`Failed to generate team code: ${response.status}`);
        }

        const data: { teamCode: string } = await response.json();
        return data.teamCode;
    }
};
