/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


<!-- Hide script from old browsers

window.onload = theClock

function theClock() {
    now = new Date;
    theDate = now.getDate()+"/"+now.getMonth()+"/"+now.getFullYear();
    theTime = ((now.getHours() > 0 && now.getHours() < 13)) ? now.getHours() : (now.getHours() == 0) ? 12 : now.getHours()-12;
    theTime += (now.getMinutes() > 9) ? ":" + now.getMinutes() : ":0" + now.getMinutes();
    theTime += (now.getSeconds() > 9) ? ":" + now.getSeconds() : ":0" + now.getSeconds();
    theTime += (now.getHours() < 12) ? " am" : " pm";
    theDateTime = theDate+"  " + theTime;
    clockSpan = document.getElementById("myClock");
    clockSpan.replaceChild(document.createTextNode(theDateTime), clockSpan.firstChild);

    setTimeout("theClock()",1000);
}

            // End hiding script from old browsers -->