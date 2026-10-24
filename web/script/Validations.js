/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

window.history.forward();
function noBack() {
    window.history.forward();
}

function printDiv(divName) {
    var printContents = document.getElementById(divName).innerHTML;
    var originalContents = document.body.innerHTML;

    document.body.innerHTML = printContents;

    window.print();

    document.body.innerHTML = originalContents;
}

function getCustomerDetails() {

    var accountNumberNew = document.getElementById("accountNumberNew").value;
   
    if (accountNumberNew == "" || accountNumberNew == null) {
        alert("Please Enter Account Number ");
        setTimeout(function () {
            document.getElementById("accountNumberNew").focus();
        }, 0);
        return false;
    }

    document.getElementById("getId").value = "get";
    document.issedBondForm.action = "successActionGet.action";
    document.issedBondForm.submit();


}


function ValidateMobile() {
    var customerName = document.getElementById("customerNameSearch").value;
    var accountNumber = document.getElementById("accountNumber").value;
    if ((customerName == null && accountNumber == null) || (customerName == "" && accountNumber == "")) {
        alert("Please enter customer name or account number")
        return false;
    }
}

function ExtraDeleteDeposits() {

    var extraIdSave = document.getElementById("extraIdSave").value;

    if (extraIdSave == "" || extraIdSave == null)
    {
        alert("Please Select Any Record ");
        setTimeout(function () {
            document.getElementById("extraIdSave").focus();
        }, 0);
        return false;
    }
    document.extraDetails.action = "extraTransactionDelete.action";
    document.extraDetails.submit();
}
function ExtraUpdateDeposits() {
    var extraPurposeSave = document.getElementById("extraPurposeSave").value;
    var extraAmountSave = document.getElementById("extraAmountSave").value;
    var extraDateSave = document.getElementById("extraDateSave").value;

    if (extraPurposeSave == "" || extraPurposeSave == null)
    {
        alert("Please enter Purpose");
        setTimeout(function () {
            document.getElementById("extraPurposeSave").focus();
        }, 0);
        return false;
    }

    if (extraAmountSave == "" || extraAmountSave == null)
    {
        alert("Please Enter Amount");
        setTimeout(function () {
            document.getElementById("extraAmountSave").focus();
        }, 0);
        return false;
    }


    if (isNaN(extraAmountSave)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("extraAmountSave").focus();
            document.getElementById("extraAmountSave").select();
        }, 0);
        return false;
    }
    if (extraDateSave == "" || extraDateSave == null)
    {
        alert("Please Select Date");
        setTimeout(function () {
            document.getElementById("extraDateSave").focus();
        }, 0);
        return false;
    }

    document.extraDetails.action = "extraTransactionUpdate.action";
    document.extraDetails.submit();
}

function  ExtraSaveDeposits() {

    var extraPurposeSave = document.getElementById("extraPurposeSave").value;
    var extraAmountSave = document.getElementById("extraAmountSave").value;
    var extraDateSave = document.getElementById("extraDateSave").value;

    if (extraPurposeSave == "" || extraPurposeSave == null)
    {
        alert("Please enter Purpose");
        setTimeout(function () {
            document.getElementById("extraPurposeSave").focus();
        }, 0);
        return false;
    }

    if (extraAmountSave == "" || extraAmountSave == null)
    {
        alert("Please Enter Amount");
        setTimeout(function () {
            document.getElementById("extraAmountSave").focus();
        }, 0);
        return false;
    }
    if (isNaN(extraAmountSave)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("extraAmountSave").focus();
            document.getElementById("extraAmountSave").select();
        }, 0);
        return false;
    }
    if (extraDateSave == "" || extraDateSave == null)
    {
        alert("Please Select Date");
        setTimeout(function () {
            document.getElementById("extraDateSave").focus();
        }, 0);
        return false;
    }

    document.extraDetails.action = "extraTransactionSave.action";
    document.extraDetails.submit();
}

function SaveMonthlyInterest() {
    var accountNumberMonth = document.getElementById("accountNumberMonth").value;
    var paymentDate = document.getElementById("paymentDate").value;
    var amountMonth = document.getElementById("amountMonth").value;

    if (accountNumberMonth == "" || accountNumberMonth == null)
    {
        alert("Please enter Account Number");
        setTimeout(function () {
            document.getElementById("accountNumberMonth").focus();
        }, 0);
        return false;
    }

    if (paymentDate == "" || paymentDate == null)
    {
        alert("Please Select Payment Date");
        setTimeout(function () {
            document.getElementById("paymentDate").focus();
        }, 0);
        return false;
    }
    if (amountMonth == "" || amountMonth == null)
    {
        alert("Please enter Amount");
        setTimeout(function () {
            document.getElementById("amountMonth").focus();
        }, 0);
        return false;
    }

    if (isNaN(amountMonth)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("amountMonth").focus();
            document.getElementById("amountMonth").select();
        }, 0);
        return false;
    }

    document.depositsDetailsMonth.action = "depositMonthPayment.action";
    document.depositsDetailsMonth.submit();
}



function SaveDeposits() {
//    var ,,
//    ,;
    var depositNameSave = document.getElementById("depositNameSave").value;
    var amountSave = document.getElementById("amountSave").value;
    var accountNumberSave = document.getElementById("accountNumberSave").value;
    var mobileNoSave = document.getElementById("mobileNoSave").value;

    var opendDateSave = document.getElementById("opendDateSave").value;
//    var closeDateSave = document.getElementById("closeDateSave").value;



    if (depositNameSave == "" || depositNameSave == null)
    {
        alert("Please enter Deposit Name");
        setTimeout(function () {
            document.getElementById("depositNameSave").focus();
        }, 0);
        return false;
    }

    if (amountSave == "" || amountSave == null)
    {
        alert("Please enter Amount ");
        setTimeout(function () {
            document.getElementById("amountSave").focus();
        }, 0);
        return false;
    }

    if (isNaN(amountSave)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("amountSave").focus();
            document.getElementById("amountSave").select();
        }, 0);
        return false;
    }


    if (accountNumberSave == "" || accountNumberSave == null)
    {
        alert("Please enter interest");
        setTimeout(function () {
            document.getElementById("accountNumberSave").focus();
        }, 0);
        return false;
    }


    if (opendDateSave == "" || opendDateSave == null)
    {
        alert("Please enter Opening Date");
        setTimeout(function () {
            document.getElementById("opendDateSave").focus();
        }, 0);
        return false;
    }


    if (mobileNoSave == "" || mobileNoSave == null)
    {
        alert("Please enter Mobile number");
        setTimeout(function () {
            document.getElementById("mobileNoSave").focus();
        }, 0);
        return false;
    }


    if (isNaN(mobileNoSave)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("mobileNoSave").focus();
            document.getElementById("mobileNoSave").select();
        }, 0);
        return false;
    }
// 


    document.depositsDetails.action = "depositDetailsSave.action";
    document.depositsDetails.submit();
}


function DeleteDeposits()
{
    var depositIdSave = document.getElementById("depositIdSave").value;
    if (depositIdSave == "" || depositIdSave == null)
    {
        alert("Please Select Any Record");
        setTimeout(function () {
            document.getElementById("depositIdSave").focus();
        }, 0);
        return false;
    }
    document.depositsDetails.action = "depositDetailsDelete.action";
    document.depositsDetails.submit();
}


function UpdateDeposits()
{

    var depositNameSave = document.getElementById("depositNameSave").value;
    var amountSave = document.getElementById("amountSave").value;
    var accountNumberSave = document.getElementById("accountNumberSave").value;
    var mobileNoSave = document.getElementById("mobileNoSave").value;

    var opendDateSave = document.getElementById("opendDateSave").value;

    var depositTypeSave = document.getElementById("depositTypeSave").value;

//    var closeDateSave = document.getElementById("closeDateSave").value;



    if (depositNameSave == "" || depositNameSave == null)
    {
        alert("Please enter Deposit Name");
        setTimeout(function () {
            document.getElementById("depositNameSave").focus();
        }, 0);
        return false;
    }

    if (amountSave == "" || amountSave == null)
    {
        alert("Please enter Amount ");
        setTimeout(function () {
            document.getElementById("amountSave").focus();
        }, 0);
        return false;
    }

    if (isNaN(amountSave)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("amountSave").focus();
            document.getElementById("amountSave").select();
        }, 0);
        return false;
    }


    if (accountNumberSave == "" || accountNumberSave == null)
    {
        alert("Please enter interest");
        setTimeout(function () {
            document.getElementById("accountNumberSave").focus();
        }, 0);
        return false;
    }


    if (opendDateSave == "" || opendDateSave == null)
    {
        alert("Please enter Opening Date");
        setTimeout(function () {
            document.getElementById("opendDateSave").focus();
        }, 0);
        return false;
    }


    if (mobileNoSave == "" || mobileNoSave == null)
    {
        alert("Please enter Mobile number");
        setTimeout(function () {
            document.getElementById("mobileNoSave").focus();
        }, 0);
        return false;
    }


    if (isNaN(mobileNoSave)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("mobileNoSave").focus();
            document.getElementById("mobileNoSave").select();
        }, 0);
        return false;
    }
// 
    if (depositTypeSave == "Out") {
        var closeDateSave = document.getElementById("closeDateSave").value;
        if (closeDateSave == "" || closeDateSave == null) {
            alert("Please Enter Closing Date");
            setTimeout(function () {
                document.getElementById("closeDateSave").focus();
            }, 0);
            return false;
        }
    }

    document.depositsDetails.action = "depositDetailsUpdate.action";
    document.depositsDetails.submit();
}

function validate() {
    var userName = document.getElementById("userName").value;
    var password = document.getElementById("password").value;
    if (userName == "" || userName == null)
    {
        alert("Please enter User Name");
        setTimeout(function () {
            document.getElementById("userName").focus();
        }, 0);
        return false;
    }

    if (password == "" || password == null)
    {
        alert("Please enter password ");
        setTimeout(function () {
            document.getElementById("password").focus();
        }, 0);
        return false;
    }
    return true;
}

function ValidateCustomerEntry() {
    var customerName = document.getElementById("customerNameSearch").value;
    var accountNumber = document.getElementById("accountNumber").value;
    var mobileNumber = document.getElementById("mobileNumber").value;

    if ((customerName == null && accountNumber == null && mobileNumber == null) || (customerName == "" && accountNumber == "" && mobileNumber == "")) {
        alert("Please enter customer name or account number or mobile Number")
        return false;
    }


    if (mobileNumber == null || mobileNumber == "") {
    } else {

        if (isNaN(mobileNumber)) {
            alert("Please enter numbers only ");
            setTimeout(function () {
                document.getElementById("mobileNumber").focus();
                document.getElementById("mobileNumber").select();
            }, 0);
            return false;
        }
//
        if (mobileNumber.length != 10) {
            alert("Mobile number should be 10 numbers only ");
            setTimeout(function () {
                document.getElementById("mobileNumber").focus();
                document.getElementById("mobileNumber").select();
            }, 0);
            return false;

        }
    }
}
function ValidateDate() {

    var startDate = document.getElementById("startDate").value;
    var endDate = document.getElementById("endDate").value;

    if (startDate == null || startDate == "") {
        alert("Please enter Start Date ");
        setTimeout(function () {
            document.getElementById("startDate").focus();
        }, 0);
        return false;
    }

    if (endDate == null || endDate == "") {
        alert("Please enter End Date ");
        setTimeout(function () {
            document.getElementById("endDate").focus();
        }, 0);
        return false;
    }


}
function ValidateSearchDeposits() {

    var depositNameSearch = document.getElementById("depositNameSearch").value;
    var depositAccountNumber = document.getElementById("depositAccountNumber").value;

    if (depositNameSearch == null && depositAccountNumber == null) {
        alert("Please enter Deposit name or Account number");
        return false;
    }


}


function validateReg() {
    var fName = document.getElementById("fName").value;
    var userName = document.getElementById("userName").value;
    var password = document.getElementById("password").value;
    var cpassword = document.getElementById("cpassword").value;
    var phoneNo = document.getElementById("phoneNo").value;
    var address = document.getElementById("address").value;

    if (fName == null || fName == "") {
        alert("Please enter first name ");
        setTimeout(function () {
            document.getElementById("fName").focus();
        }, 0);
        return false;
    }
//
    if (userName == null || userName == "") {
        alert("Please enter user  name ");
        setTimeout(function () {
            document.getElementById("userName").focus();
        }, 0);
        return false;
    }
//
    if (userName.length < 6) {
        alert("User Name should contain more than 6 letters");
        setTimeout(function () {
            document.getElementById("userName").focus();
            document.getElementById("userName").select();
        }, 0);
        return false;
    }
//

    if (password == null || password == "") {
        alert("Please enter password ");
        setTimeout(function () {
            document.getElementById("password").focus();
        }, 0);
        return false;
    }
//
    if (password.length < 6) {
        alert("password should contain more than 6 letters");
        setTimeout(function () {
            document.getElementById("password").focus();
            document.getElementById("password").select();
        }, 0);
        return false;
    }
//
    if (cpassword == null || cpassword == "") {
        alert("Please enter confirm password ");
        setTimeout(function () {
            document.getElementById("cpassword").focus();
        }, 0);
        return false;
    }
    if (cpassword.length < 6) {
        alert("Confirm password should contain more than 6 letters");
        setTimeout(function () {
            document.getElementById("cpassword").focus();
            document.getElementById("cpassword").select();
        }, 0);
        return false;
    }
//
    if (cpassword != password) {
        alert("Password and confirm password should be same ");
        setTimeout(function () {
            document.getElementById("cpassword").focus();
            document.getElementById("cpassword").select();
        }, 0);
        return false;
    }
//
    if (phoneNo == null || phoneNo == "") {
        alert("Please enter phone number ");
        setTimeout(function () {
            document.getElementById("phoneNo").focus();
        }, 0);
        return false;
    }
//
    if (isNaN(phoneNo)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("phoneNo").focus();
            document.getElementById("phoneNo").select();
        }, 0);
        return false;
    }
//
    if (phoneNo.length != 10) {
        alert("phone number should be 10 numbers only ");
        setTimeout(function () {
            document.getElementById("phoneNo").focus();
            document.getElementById("phoneNo").select();
        }, 0);
        return false;

    }
//
    if (address == null || address == "") {
        alert("Please enter Address ");
        setTimeout(function () {
            document.getElementById("address").focus();
        }, 0);
        return false;
    }
//    return true;
}


function  validateDailySheet() {

    var dateId = document.getElementById("dateId").value;
    var paidAmount = document.getElementById("paidAmount").value;
    var accountNumber = document.getElementById("accountNumber").value;
    var customerName = document.getElementById("customerName").value;
    var installment = document.getElementById("installment").value;

    if (dateId == null || dateId == "") {
        alert("Please Select Date ");
        setTimeout(function () {
            document.getElementById("dateId").focus();
        }, 0);
        return false;
    }

    if (accountNumber == null || accountNumber == "") {
        alert("Please enter Account Number ");
        setTimeout(function () {
            document.getElementById("accountNumber").focus();
        }, 0);
        return false;
    }




    if (customerName == null || customerName == "") {
        alert("Please enter Customer Name ");
        setTimeout(function () {
            document.getElementById("customerName").focus();
        }, 0);
        return false;
    }


    if (paidAmount == null || paidAmount == "") {
        alert("Please enter Amount");
        setTimeout(function () {
            document.getElementById("paidAmount").focus();
        }, 0);
        return false;
    }

    if (isNaN(paidAmount)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("paidAmount").focus();
            document.getElementById("paidAmount").select();
        }, 0);
        return false;
    }

    if (installment == null || installment == "") {
        alert("Please enter installment ");
        setTimeout(function () {
            document.getElementById("installment").focus();
        }, 0);
        return false;
    }

}

function ValidateCustomerUpdate(value, from) {
    var dateId1 = document.getElementById("dateId1").value;
    var customerNameN = document.getElementById("customerName").value;
    var occupation = document.getElementById("customerOccupation").value;
    var address = document.getElementById("customerAddres").value;
    var mobileNumber = document.getElementById("customerMobileNo").value;
    if (dateId1 == null || dateId1 == "") {
        alert("Please select Date ");
        setTimeout(function () {
            document.getElementById("dateId1").focus();
        }, 0);
        return false;
    }
//
    if (customerNameN == null || customerNameN == "") {
        alert("Please enter Customer Name ");
        setTimeout(function () {
            document.getElementById("customerName").focus();
        }, 0);
        return false;
    }

    if (occupation == null || occupation == "") {
        alert("Please enter Occupation ");
        setTimeout(function () {
            document.getElementById("customerOccupation").focus();
        }, 0);
        return false;
    }

    if (address == null || address == "") {
        alert("Please enter Customer Address ");
        setTimeout(function () {
            document.getElementById("customerAddres").focus();
        }, 0);
        return false;
    }

    if (mobileNumber == null || mobileNumber == "") {
        alert("Please enter Mobile Number  ");
        setTimeout(function () {
            document.getElementById("customerMobileNo").focus();
        }, 0);
        return false;
    }


    if (isNaN(mobileNumber)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("customerMobileNo").focus();
            document.getElementById("customerMobileNo").select();
        }, 0);
        return false;
    }
//
    if (mobileNumber.length != 10) {
        alert("phone number should be 10 numbers only ");
        setTimeout(function () {
            document.getElementById("customerMobileNo").focus();
            document.getElementById("customerMobileNo").select();
        }, 0);
        return false;

    }

    var suretyAddress = document.getElementById("suretyAddress").value;
    var suretyOccupation = document.getElementById("suretyOccupation").value;
    var suretyName = document.getElementById("suretyName").value;
    var suretyMobileNumber = document.getElementById("suretyMobileNo").value;

    if (suretyName == null || suretyName == "") {
        alert("Please enter Customer Name ");
        setTimeout(function () {
            document.getElementById("suretyName").focus();
        }, 0);
        return false;
    }

    if (suretyOccupation == null || suretyOccupation == "") {
        alert("Please enter Occupation ");
        setTimeout(function () {
            document.getElementById("suretyOccupation").focus();
        }, 0);
        return false;
    }

    if (suretyAddress == null || suretyAddress == "") {
        alert("Please enter Surety Address ");
        setTimeout(function () {
            document.getElementById("suretyAddress").focus();
        }, 0);
        return false;
    }

    if (suretyMobileNumber == null || suretyMobileNumber == "") {
        alert("Please enter Mobile Number  ");
        setTimeout(function () {
            document.getElementById("suretyMobileNo").focus();
        }, 0);
        return false;
    }


    if (isNaN(suretyMobileNumber)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("suretyMobileNo").focus();
            document.getElementById("suretyMobileNo").select();
        }, 0);
        return false;
    }
//
    if (suretyMobileNumber.length != 10) {
        alert("phone number should be 10 numbers only ");
        setTimeout(function () {
            document.getElementById("suretyMobileNo").focus();
            document.getElementById("suretyMobileNo").select();
        }, 0);
        return false;

    }


    var installmentsN = document.getElementById("installments").value;
    var originalAmount = document.getElementById("originalAmount").value;
    var accountNumberNew = document.getElementById("accountNumberNew").value;


    if (accountNumberNew == null || accountNumberNew == "") {
        alert("Please enter Account number ");
        setTimeout(function () {
            document.getElementById("accountNumberNew").focus();
        }, 0);
        return false;
    }


    if (originalAmount == null || originalAmount == "") {
        alert("Please enter Original Amount ");
        setTimeout(function () {
            document.getElementById("originalAmount").focus();
        }, 0);
        return false;
    }

    if (installmentsN == null || installmentsN == "") {
        alert("Please enter Mobile Number  ");
        setTimeout(function () {
            document.getElementById("installments").focus();
        }, 0);
        return false;
    }



    if (isNaN(installmentsN)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("installments").focus();
            document.getElementById("installments").select();
        }, 0);
        return false;
    }

    if (isNaN(originalAmount)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("originalAmount").focus();
            document.getElementById("originalAmount").select();
        }, 0);
        return false;
    }

    if (value == "Delete") {
        if (from == "Open") {
            document.customerForm.action = "customerDetailsDelete.action";
            document.customerForm.submit();
        } else {
            document.customerForm.action = "customerDetailsDeleteClose.action";
            document.customerForm.submit();
        }

    } else {
        if (from == "Open") {
            document.customerForm.action = "customerDetailsUpdates.action";
            document.customerForm.submit();
        } else {
            document.customerForm.action = "customerDetailsUpdatesClose.action";
            document.customerForm.submit();
        }


    }
}


function ValidateCustomer() {

    var dateId1 = document.getElementById("dateId1").value;
    var customerNameN = document.getElementById("customerNameN").value;
    var occupation = document.getElementById("occupation").value;
    var address = document.getElementById("address").value;
    var mobileNumber = document.getElementById("mobileNumber").value;

    if (dateId1 == null || dateId1 == "") {
        alert("Please select Date ");
        setTimeout(function () {
            document.getElementById("dateId1").focus();
        }, 0);
        return false;
    }
//
    if (customerNameN == null || customerNameN == "") {
        alert("Please enter Customer Name ");
        setTimeout(function () {
            document.getElementById("customerNameN").focus();
        }, 0);
        return false;
    }

    if (occupation == null || occupation == "") {
        alert("Please enter Occupation ");
        setTimeout(function () {
            document.getElementById("occupation").focus();
        }, 0);
        return false;
    }

    if (address == null || address == "") {
        alert("Please enter Customer Address ");
        setTimeout(function () {
            document.getElementById("address").focus();
        }, 0);
        return false;
    }

    if (mobileNumber == null || mobileNumber == "") {
        alert("Please enter Mobile Number  ");
        setTimeout(function () {
            document.getElementById("mobileNumber").focus();
        }, 0);
        return false;
    }


    if (isNaN(mobileNumber)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("mobileNumber").focus();
            document.getElementById("mobileNumber").select();
        }, 0);
        return false;
    }
//
    if (mobileNumber.length != 10) {
        alert("phone number should be 10 numbers only ");
        setTimeout(function () {
            document.getElementById("mobileNumber").focus();
            document.getElementById("mobileNumber").select();
        }, 0);
        return false;

    }




    var suretyAddress = document.getElementById("suretyAddress").value;
    var suretyOccupation = document.getElementById("suretyOccupation").value;
    var suretyName = document.getElementById("suretyName").value;
    var suretyMobileNumber = document.getElementById("suretyMobileNumber").value;

    if (suretyName == null || suretyName == "") {
        alert("Please enter Customer Name ");
        setTimeout(function () {
            document.getElementById("suretyName").focus();
        }, 0);
        return false;
    }

    if (suretyOccupation == null || suretyOccupation == "") {
        alert("Please enter Occupation ");
        setTimeout(function () {
            document.getElementById("suretyOccupation").focus();
        }, 0);
        return false;
    }

    if (suretyAddress == null || suretyAddress == "") {
        alert("Please enter Surety Address ");
        setTimeout(function () {
            document.getElementById("suretyAddress").focus();
        }, 0);
        return false;
    }

    if (suretyMobileNumber == null || suretyMobileNumber == "") {
        alert("Please enter Mobile Number  ");
        setTimeout(function () {
            document.getElementById("suretyMobileNumber").focus();
        }, 0);
        return false;
    }


    if (isNaN(suretyMobileNumber)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("suretyMobileNumber").focus();
            document.getElementById("suretyMobileNumber").select();
        }, 0);
        return false;
    }
//
    if (suretyMobileNumber.length != 10) {
        alert("phone number should be 10 numbers only ");
        setTimeout(function () {
            document.getElementById("suretyMobileNumber").focus();
            document.getElementById("suretyMobileNumber").select();
        }, 0);
        return false;

    }


    var installmentsN = document.getElementById("installmentsN").value;
    var originalAmount = document.getElementById("originalAmount").value;
    var amountNew = document.getElementById("amountNew").value;
    var accountNumberNew = document.getElementById("accountNumberNew").value;


    if (accountNumberNew == null || accountNumberNew == "") {
        alert("Please enter Account number ");
        setTimeout(function () {
            document.getElementById("accountNumberNew").focus();
        }, 0);
        return false;
    }


    if (amountNew == null || amountNew == "") {
        alert("Please enter Amount ");
        setTimeout(function () {
            document.getElementById("amountNew").focus();
        }, 0);
        return false;
    }

    if (originalAmount == null || originalAmount == "") {
        alert("Please enter Original Amount ");
        setTimeout(function () {
            document.getElementById("originalAmount").focus();
        }, 0);
        return false;
    }

    if (installmentsN == null || installmentsN == "") {
        alert("Please enter Mobile Number  ");
        setTimeout(function () {
            document.getElementById("installmentsN").focus();
        }, 0);
        return false;
    }





    if (isNaN(installmentsN)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("installmentsN").focus();
            document.getElementById("installmentsN").select();
        }, 0);
        return false;
    }

    if (isNaN(originalAmount)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("originalAmount").focus();
            document.getElementById("originalAmount").select();
        }, 0);
        return false;
    }
    if (isNaN(amountNew)) {
        alert("Please enter numbers only ");
        setTimeout(function () {
            document.getElementById("amountNew").focus();
            document.getElementById("amountNew").select();
        }, 0);
        return false;
    }

}

 