/*!
 * SignerApp JavaScript Library v1.0
 * 
 * Author: Gurmeet Singh (gsdhillon@gmail.com), Computer Division, BARC, Mumbai.
 * Date: 2019-07-28
 */
function getSampleData(){
    var sampleData = 
        '\"Application\": {' +
        '\n\t\"Header\" : \"Procurement of Items\", ' +
        '\n\t\"RefNo\" : 12345, ' +
        '\n\t\"Applicant\" : {"Empno\" : \"21442\", "Name\" : \"Renuka Ghate\" }, ' +
        '\n\t\"ApplyDate\" : \"24/07/2019 10:24 hrs\", ' +
        '\n\t\"Items\" : ['+
        '\n\t\t{\"SNo\" : 1, \"Desc\" : \"Server m/c\", \"Price\" : 550000 }, '+
        '\n\t\t{\"SNo\" : 2, \"Desc\" : \"Network Switch\", \"Price\" : 50000 }'+
        '\n\t]'+
        '\n}';  
    return sampleData;
    
}
function makeJsonDataToSign(applicatioData, empno, name, role, action, remarks, certsno, datetime){
    var jsonDataToSign = 
        '{'+
        '\n' + applicatioData+',' +
        '\n\"'+role+'\" : {'+
        '\n\t\"Empno\" : \"'+empno+'\",'+
        '\n\t\"Name\" : \"'+name+'\", '+
        '\n\t\"Action\" : \"'+action+'\", '+
        '\n\t\"Remarks\" : \"'+remarks+'\", '+
//        '\n\t\"CertSno\" : \"'+certsno+'\",'+
        '\n\t\"SignDate\" : \"'+datetime+'\"'+
        '\n}' +
        '\n}'; 

    return jsonDataToSign;
}
function makeReqData(empno, certsno, data){
    var dataBase64 = btoa(data);
    //= may present in base64 so use :
    var reqData = 
        "EMP_NO:"+empno+","+
        "CERT_SNO:"+certsno+","+
        "DATA:"+dataBase64;
    console.log( "SignerApp req data: "+ reqData );
    return reqData;
}

function checkAndSend(jsonObject, receiveSign){
   // receiveSign(JSON.stringify(jsonObject));
    if(jsonObject.status == "SUCCESS"){
        receiveSign(jsonObject.result); 
    }else{
        alert(jsonObject.result);
        receiveSign("");
    } 
}
//this method does not require jquery-3.0.0.js
function signWithMehtod1(applicatioData, empno, name, role, action, remarks, certsno, datetime, receiveSign){
    console.log('called function signWithMehtod1()');
    //
    var data = makeJsonDataToSign(applicatioData, empno, name, role, action, remarks, certsno, datetime);
    //POST request
    var url = "http://localhost:8000/SignerApp";
    var reqData = makeReqData(empno, certsno, data);    
    var xhr = new XMLHttpRequest(); 
    xhr.open("POST", url);  
    xhr.send(reqData);
    xhr.onload = function() {
        console.log( "SignerApp xhr.status: "+ xhr.status );
        if(xhr.status == 200 && xhr.response){
            var jsonObject = JSON.parse( xhr.response );
            checkAndSend(jsonObject, receiveSign);
        }else{
           alert("Signing failed Mehtod1 ! ");
           receiveSign("");
        }
    };
    xhr.onerror = function() { // only triggers if the request couldn't be made at all
        alert('Network Error');
    };
    xhr.onprogress = function(event) { // triggers periodically
        // event.loaded - how many bytes downloaded
        // event.lengthComputable = true if the server sent Content-Length header
        // event.total - total number of bytes (if lengthComputable)
        console.log('Received '+event.loaded +' of '+event.total);
    };
}
//require jquery-3.0.0.js
function signWithMethod2(applicatioData, empno, name, role, action, remarks, certsno, datetime, receiveSign){
    console.log('called function signWithMehtod2()');
    //
    var data = makeJsonDataToSign(applicatioData, empno, name, role, action, remarks, certsno, datetime);
    //GET Request
//    var url = "http://localhost:8000/SignerApp?"+makeReqData(empno, certsno, data);
    var url = "http://localhost:8000/SignerApp";
    $.ajax({url: url, dataType: 'html', data: makeReqData(empno, certsno, data), success: function(data, status) {
            console.log( "SignerApp $.ajax status: "+ status );
            if("success" == status && data){
                var jsonObject = JSON.parse( data );
                checkAndSend(jsonObject, receiveSign);
            }else{
                alert("Signing failed Mehtod2 ! ");
                receiveSign("");
            }

    }}); 
}
//require jquery-3.0.0.js
function signWithMethod3(applicatioData, empno, name, role, action, remarks, certsno, datetime, receiveSign){
    console.log('called function signWithMehtod3()');
    //
    var data = makeJsonDataToSign(applicatioData, empno, name, role, action, remarks, certsno, datetime);
    //POST Request
    var url = "http://localhost:8000/SignerApp";
    //
    $.post( url, makeReqData(empno, certsno, data), function(jsonObject, status) {
        console.log( "SignerApp $.post status: "+ status);
        if("success" == status && jsonObject){
            checkAndSend(jsonObject, receiveSign);
        }else{
            alert("Signing failed Mehtod3 ! ");
            receiveSign("");
        }
    });
}