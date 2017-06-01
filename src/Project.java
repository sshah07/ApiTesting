
/**
 * Created by shachi on 5/30/17.
 */
public class Project {
	public Long id;
	public String projectName;
    public String creationDate;
    public String expiryDate;
    public boolean enabled;
    public String[] targetCountries;
    public String projectUrl;
	public double projectCost;
    public TargetKeys[] targetKeys;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String[] getTargetCountries() {
        return targetCountries;
    }

    public void setTargetCountries(String[] targetCountries) {
        this.targetCountries = targetCountries;
    }
    public String getProjectUrl() {
		return projectUrl;
	}

	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}


    public double getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(double projectCost) {
        this.projectCost = projectCost;
    }

    public TargetKeys[] getTargetKeys() {
        return targetKeys;
    }

    public void setTargetKeys(TargetKeys[] targetKeys) {
        this.targetKeys = targetKeys;
    }
}
