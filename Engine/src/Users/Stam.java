package Users;

public class Stam {
}

/*
// CLASSES IN THE ENGINE:
Class UBoat implements Entity{
	private String userName;
	private UBoatEngineManager engineManager;
	private List<Allie> allAllies;
    private List<boolean> isReady;
}

Class Agent implements Entity{
	private String userName;
	private Enigma engigma; // maybe not needed.
}

Class Allie implements Entity {
	private String userName;
	private List<Agent> allAgents;
	private DM dm;
}

Class Battlefield{
	private String name;
	private String level;
	private int alliesTeamNumber;
}

Class Contest{
	private Battlefield battlefield;
	private UBoat uboat;
}

Class UserManager{
	private Map<String,Uboat> allUboats;
	private Map<String,Allie> allAllies;
	private Map<String,Agent> allAgents;
}

// DATA THAT THE SERVLET CONTEXT WILL HOLD
Class ServletContext{
	private UserManager userManager;
	// private Set<Contest> allContests;
	private Set<String> allContests;
}

// which data should be sent on the session?
// is the userManager should be Map<String, Entity> ?


// Agent (AllieName on session) -> go to userManager & get the Allie
// Allie -> (AllieName on session) -> go to userManager & get the Allie -> contact all Agetns
// Uboat -> (UboatName on session) -> go to userManager & get the Uboat -> contact all allies



*/