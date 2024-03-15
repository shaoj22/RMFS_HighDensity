package MultiCooperation;

public interface MultiCooperationStrategy {
	public String DeadLockDetectionAndRecovery = "DR";
	public String FuturePathPlanningWithRecovery = "DR&FPP";
	public String DeadlockAvoidance = "DA";
	public String FuturePathPlanningWithAvoidance = "DA&FPP";
	public String HistoryBasedPathPlanning = "HBPP";
}
