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

// Red color instead of default blue
function changeColor() {
    document.querySelectorAll('.chip').forEach(chip => {
        chip.classList.add('red');
    })
}

/*
 * Extracts ingredients inside chips search bar
 */
function searchIngredients() {
	let chips = M.Chips.getInstance($(".chips")).chipsData;
	let ingredientes = "";
	
	chips.forEach(function(ingredient) {
		ingredientes += ingredient.tag + " ";
	});
	
	ingredientes = ingredientes.trim();
	
	document.buscador.ingredientes.value = ingredientes;
	
	document.buscador.submit();
}

function editIngredients() {
	let ingredients = $(".ingredientes")
	let instance = M.Chips.getInstance($(".chips"));
	
	
	$.each(ingredients, function(key, ingredient) {
		instance.addChip({
		    tag: ingredient.textContent.trim()
		  });
	});
}
