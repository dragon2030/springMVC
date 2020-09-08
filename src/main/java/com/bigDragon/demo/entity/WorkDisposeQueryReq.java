package com.bigDragon.demo.entity;


import java.util.Date;
import java.util.List;


/**
 * @createTime 2016-04-06
 * @author pengtt
 * 
 */
public class WorkDisposeQueryReq{
	private String workDisposeId;       //综合受理id
	private String workdisposeType;     //受理类别
	private Date workdisposeDate;       //受理日期
	private String workdisposeOpenType; //开户类型
	private String workdisposeOperator; //操作人
	private String workdisposeCost;     //总费用
	private String workdisposeFeatures; //特征
	private String workdisposeRemark;   //受理备注
	private Date createTime;            //创建时间
	private String workOrderId;         //工单id
	private String addrConstructionTeam;//施工班组
	private String addrId;              //地址id
	private String addrDes;             //地址描述
	private String userNo;              //用户号
	private String custId;              //客户Id
	private String custName;            //客户名称

	private String corpId;              //所属公司
	private String deptId;              //所属部门
	private String tenantId;            //租户ID
	private String orgId;
	//残困信息
	private String deformedId;//残困id
	private String deformedName;//残困证姓名
	private String deformedCard;//残困证号
	private String deformedIdCard;//残困证身份证号
	
	//其他收费list
	private List<User> otherCostRecordDtoList;
	//工单id
	private String WoId;
	
	private String isSendMessage;
	
	private String custMobile;//短信手机号

	public String nineteenFlag;//1:19厅新建


	private String payMode;//支付方式
	private String realAmt;//实际支付金额


	private String workdisposeDateStart;
	private String workdisposeDateEnd;

	private String staffNo;
	private String addrDeptId;


	private Date printTime;                 //打印时间
	private String printStaff;              //打印人
	private String printStaffName;              //打印人姓名

	private String isValid;

	private String workDisposeIdNot;       //不包含的综合受理id


	private String isFeng;
	private String isHotwithopen;//是否户热同开



	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getRealAmt() {
		return realAmt;
	}

	public void setRealAmt(String realAmt) {
		this.realAmt = realAmt;
	}

	public String getWorkDisposeId() {
		return workDisposeId;
	}
	public void setWorkDisposeId(String workDisposeId) {
		this.workDisposeId = workDisposeId;
	}
	public String getWorkdisposeType() {
		return workdisposeType;
	}
	public void setWorkdisposeType(String workdisposeType) {
		this.workdisposeType = workdisposeType;
	}
	public Date getWorkdisposeDate() {
		return workdisposeDate;
	}
	public void setWorkdisposeDate(Date workdisposeDate) {
		this.workdisposeDate = workdisposeDate;
	}
	public String getWorkdisposeOpenType() {
		return workdisposeOpenType;
	}
	public void setWorkdisposeOpenType(String workdisposeOpenType) {
		this.workdisposeOpenType = workdisposeOpenType;
	}
	public String getWorkdisposeOperator() {
		return workdisposeOperator;
	}
	public void setWorkdisposeOperator(String workdisposeOperator) {
		this.workdisposeOperator = workdisposeOperator;
	}
	public String getWorkdisposeCost() {
		return workdisposeCost;
	}
	public void setWorkdisposeCost(String workdisposeCost) {
		this.workdisposeCost = workdisposeCost;
	}
	public String getWorkdisposeFeatures() {
		return workdisposeFeatures;
	}
	public void setWorkdisposeFeatures(String workdisposeFeatures) {
		this.workdisposeFeatures = workdisposeFeatures;
	}
	public String getWorkdisposeRemark() {
		return workdisposeRemark;
	}
	public void setWorkdisposeRemark(String workdisposeRemark) {
		this.workdisposeRemark = workdisposeRemark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getWorkOrderId() {
		return workOrderId;
	}
	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	
	public String getAddrConstructionTeam() {
		return addrConstructionTeam;
	}
	public void setAddrConstructionTeam(String addrConstructionTeam) {
		this.addrConstructionTeam = addrConstructionTeam;
	}
	public String getAddrId() {
		return addrId;
	}
	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}
	public String getAddrDes() {
		return addrDes;
	}
	public void setAddrDes(String addrDes) {
		this.addrDes = addrDes;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDeformedId() {
		return deformedId;
	}
	public void setDeformedId(String deformedId) {
		this.deformedId = deformedId;
	}
	public String getDeformedName() {
		return deformedName;
	}
	public void setDeformedName(String deformedName) {
		this.deformedName = deformedName;
	}
	public String getDeformedCard() {
		return deformedCard;
	}
	public void setDeformedCard(String deformedCard) {
		this.deformedCard = deformedCard;
	}
	public String getDeformedIdCard() {
		return deformedIdCard;
	}
	public void setDeformedIdCard(String deformedIdCard) {
		this.deformedIdCard = deformedIdCard;
	}
	public List<User> getOtherCostRecordDtoList() {
		return otherCostRecordDtoList;
	}
	public void setOtherCostRecordDtoList(List<User> otherCostRecordDtoList) {
		this.otherCostRecordDtoList = otherCostRecordDtoList;
	}
	public String getWoId() {
		return WoId;
	}
	public void setWoId(String woId) {
		WoId = woId;
	}
	public String getIsSendMessage() {
		return isSendMessage;
	}
	public void setIsSendMessage(String isSendMessage) {
		this.isSendMessage = isSendMessage;
	}
	public String getCustMobile() {
		return custMobile;
	}
	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public String getNineteenFlag() {
		return nineteenFlag;
	}

	public void setNineteenFlag(String nineteenFlag) {
		this.nineteenFlag = nineteenFlag;
	}

	public String getWorkdisposeDateStart() {
		return workdisposeDateStart;
	}

	public void setWorkdisposeDateStart(String workdisposeDateStart) {
		this.workdisposeDateStart = workdisposeDateStart;
	}

	public String getWorkdisposeDateEnd() {
		return workdisposeDateEnd;
	}

	public void setWorkdisposeDateEnd(String workdisposeDateEnd) {
		this.workdisposeDateEnd = workdisposeDateEnd;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getAddrDeptId() {
		return addrDeptId;
	}

	public void setAddrDeptId(String addrDeptId) {
		this.addrDeptId = addrDeptId;
	}

	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getPrintStaff() {
		return printStaff;
	}

	public void setPrintStaff(String printStaff) {
		this.printStaff = printStaff;
	}

	public String getPrintStaffName() {
		return printStaffName;
	}

	public void setPrintStaffName(String printStaffName) {
		this.printStaffName = printStaffName;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getWorkDisposeIdNot() {
		return workDisposeIdNot;
	}

	public void setWorkDisposeIdNot(String workDisposeIdNot) {
		this.workDisposeIdNot = workDisposeIdNot;
	}

	public String getIsFeng() {
		return isFeng;
	}

	public void setIsFeng(String isFeng) {
		this.isFeng = isFeng;
	}

	public String getIsHotwithopen() {
		return isHotwithopen;
	}

	public void setIsHotwithopen(String isHotwithopen) {
		this.isHotwithopen = isHotwithopen;
	}
	private String userId;
	private String name;
	private String age;
	private String peopleDes;
	private String sexId;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPeopleDes() {
		return peopleDes;
	}
	public void setPeopleDes(String peopleDes) {
		this.peopleDes = peopleDes;
	}
	public String getSexId() {
		return sexId;
	}
	public void setSexId(String sexId) {
		this.sexId = sexId;
	}
	
	
}
