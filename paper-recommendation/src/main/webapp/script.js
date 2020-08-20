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
/*
$('.like, .dislike').on('click', function() {
    event.preventDefault();
    $('.active').removeClass('active');
    $(this).addClass('active');
});
document.querySelector(".like, .dislike")
    .addEventListener("click", function(){
        event.preventDefault();
        document.querySelector(".active").classList.remove("active");
        this.classList.add(".active");

    });
*/
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


