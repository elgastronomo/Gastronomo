/*
   _____           _                                         
  / ____|         | |                                        
 | |  __  __ _ ___| |_ _ __ ___  _ __   ___  _ __ ___   ___  
 | | |_ |/ _` / __| __| '__/ _ \| '_ \ / _ \| '_ ` _ \ / _ \ 
 | |__| | (_| \__ \ |_| | | (_) | | | | (_) | | | | | | (_) |
  \_____|\__,_|___/\__|_|  \___/|_| |_|\___/|_| |_| |_|\___/ 
                                                             

Facultad de Informática - Universidad Complutense de Madrid
Ingeniería Web

David Arroyo Segovia
David Losila Cadenas                                                                                                                 
Roberto Fernández Correa

*/

M.AutoInit();

document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.chips');
    var instances = M.Chips.init(elems, { onChipAdd: changeColor });

});

// Color rojo en vez del azul por defecto
function changeColor() {
    document.querySelectorAll('.chip').forEach(chip => {
        chip.classList.add('red');
    })
}
