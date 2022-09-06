package pojoDeSerialize;

import java.util.List;

public class CoursesJson 
{
	private List<WebAutomationJson> webAutomation;
	private List<ApiJson> api;
	private List<MobileJson> mobile;
	
	public List<WebAutomationJson> getWebAutomation() {
		return webAutomation;
	}
	public void setWebAutomation(List<WebAutomationJson> webAutomation) {
		this.webAutomation = webAutomation;
	}
	public List<ApiJson> getApi() {
		return api;
	}
	public void setApi(List<ApiJson> api) {
		this.api = api;
	}
	public List<MobileJson> getMobile() {
		return mobile;
	}
	public void setMobile(List<MobileJson> mobile) {
		this.mobile = mobile;
	}
}
