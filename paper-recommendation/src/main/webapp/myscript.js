// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function myFunction() {
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    ul = document.getElementById("myUL");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}

function search() {
  fetch('/data').then(response => response.json()).then((tasks) => {

    // Build the list of history entries.
    const historyEl = document.getElementById('history');
    tasks.forEach((title) => {
      historyEl.appendChild(createListElement(title));
    });
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function myonload(){
    document.cookie = 'cookie2=value2; SameSite=None; Secure';
    listAllDoc();
    //search();
    searchBar();
}
   
function searchBar() {
    var id = '0ac45842-e353-11ea-a223-0242ac130002';
    var ci_search = document.createElement('script');
    ci_search.type = 'text/javascript';
    ci_search.async = true;
    ci_search.src = 'https://cse.expertrec.com/api/js/ci_common.js?id=' + id;
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(ci_search, s);
}
    
function listAllDoc(){
    //var f = fetch('config.json');
    console.log('hello');
    
		fetch('config.json').then((r) => r.json()).then((json) =>{  
			var keys = Object.keys(json);
			var html="";
            html += "<ul id=\"myUL\">"

			for (var i=0; i< keys.length; i++){
				var keyHeader="<h3 style=\"padding: 0px 15px\">"+keys[i]+"</h3>"
				html += keyHeader;
				for(var j=0; j<json[keys[i]].length; j++){
					path = "config?id="+json[keys[i]][j]['id'];
  					html+="<li><a href="+path + ">"+ json[keys[i]][j]['title'] +"</a></li>"
				}

			}
			html += "</ul>"

    		document.getElementById('all-paper').innerHTML=html
            
		});
}
