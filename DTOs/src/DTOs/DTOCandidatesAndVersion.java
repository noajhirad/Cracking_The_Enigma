package DTOs;

import java.util.List;

public class DTOCandidatesAndVersion {
    List<DTOBruteForceResult> candidates;
    int version;

    public DTOCandidatesAndVersion(List<DTOBruteForceResult> candidates, int version) {
        this.candidates = candidates;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public List<DTOBruteForceResult> getCandidates() {
        return candidates;
    }
}
