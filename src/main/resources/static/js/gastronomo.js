	/*
   _____           _                                         
  / ____|         | |                                        
 | |  __  __ _ ___| |_ _ __ ___  _ __   ___  _ __ ___   ___  
 | | |_ |/ _` / __| __| '__/ _ \| '_ \ / _ \| '_ ` _ \ / _ \ 
 | |__| | (_| \__ \ |_| | | (_) | | | | (_) | | | | | | (_) |
  \_____|\__,_|___/\__|_|  \___/|_| |_|\___/|_| |_| |_|\___/ 
                                                             

Facultad de Informática - Universidad Complutense de Madrid
Ingeniería Web

Roberto Fernández Correa
David Arroyo Segovia
David Losila Cadenas                                                                                                                 


*/

M.AutoInit();

// Add recipe tags autocomplete
let autocompleteOptions = {
	data: {
		'Sin huevo': null,
		'Libre de nueces y de cacahuetes': null,
		'Sin frutos secos': null,
		'Sin soja': null,
		'Sin pescado': null,
		'Sin mariscos': null,
		'Bajo en carbohidratos': null,
		'Vegetariano': null,
		'Sin glutén': null,
		'Equilibrada': null,
		'Mucha fibra': null,
		'Bajo en sodio': null,
		'Vegano': null,
		'Sin lácteos': null,
		'Paleo': null,
		'Alto valor proteíco': null,
		'Bajo en grasas': null
	},
	limit: Infinity,
	minLength: 1
};

// Initialize all elements
document.addEventListener('DOMContentLoaded', function() {
	addIndexFiltersListeners();
	addFavTagListener();
	addFavIngredientListener();
	addDeleteCommentId();
	
	let elems = document.querySelectorAll('.chips');
	let instances = M.Chips.init(elems, { onChipAdd: changeColor });

	elems = document.querySelectorAll('.chips-ingredientes');
	instances = M.Chips.init(elems, {
		placeholder: '+Ingrediente',
		secondaryPlaceholder: '+Ingrediente',
		onChipDelete: removeFavIngredient
	});
	if (elems.length != 0){
		getFavouriteIngredients();
	}

	elems = document.querySelectorAll('.chips-tags');
	instances = M.Chips.init(elems, {
		placeholder: '+Tag',
		secondaryPlaceholder: '+Tag',
		autocompleteOptions: autocompleteOptions,
		onChipDelete: removeFavTag
	});
	if (elems.length != 0){
		getFavouriteTags();
	}
	
});

/*******************************************************
 * MAIN INGREDIENT SEARCHER
 *******************************************************/

/*
 * Extracts ingredients inside chips search bar
 */
 function searchIngredients() {
 	let chips = M.Chips.getInstance($(".buscador .chips")).chipsData;
 	let ingredientes = "";
 	
 	chips.forEach(function(ingredient) {
 		ingredientes += ingredient.tag + " ";
 	});
 	
 	ingredientes = ingredientes.trim();
 	
 	document.buscador.ingredientes.value = ingredientes;
 	
 	document.buscador.submit();
 }

 // Load ingredients and show red chips
 function loadIngredients() {
 	let ingredients = $(".ingredientes")
 	let instance = M.Chips.getInstance($(".chips"));
 	
 	
 	$.each(ingredients, function(key, ingredient) {
 		instance.addChip({
 			tag: ingredient.textContent.trim()
 		});
 	});
 }

 // Homepage filters
 function addIndexFiltersListeners() {
 	addFilter("difficulty", "facil", "Fácil");
 	addFilter("difficulty", "medio", "Medio");
 	addFilter("difficulty", "dificil", "Difícil");
 	
 	addFilter("tiempo", "tiempo-1", "20");
 	addFilter("tiempo", "tiempo-2", "40");
 	addFilter("tiempo", "tiempo-3", "80");
 	
 	addFilter("cuisine", "espanyola", "Española");
 	addFilter("cuisine", "italiana", "Italiana");
 	addFilter("cuisine", "griega", "Griega");
 	addFilter("cuisine", "mejicana", "Mejicana");
 }

 // Aux function homepage filters
 function addFilter(inputName, buttonId, value) {
 	let button = $("#" + buttonId);
 	button.click(() => {
 		let active = false;
 		$('form[name="buscador"] input[name=' + inputName + ']').each(
 			function(index){  
 				let input = $(this);
 				active = input.val() == value;
 			}
 			);
 		
 		if (active) {
 			button.parent().css("background-color", "");
 			button.parent().css("border-radius", "");
 			$('form[name="buscador"] input[name=' + inputName + ']').remove();
 		}
 		else {
 			button.parent().css("background-color", "#ef5350");
 			button.parent().css("border-radius", "50px");
 			$('form[name="buscador"]').append('<input type="hidden" name="' + inputName + '" value="' + value + '" />');
 		}		
 	});
 }
 
 /*******************************************************
  * SINGLE RECIPE PAGE
  *******************************************************/
 
 function addDeleteCommentId() {
	 
	 $("a[href$='#eliminar-comentario']").click((elem) => {
		 let commentId;
		 commentId = elem.currentTarget.id;
		 
		 $("#link-borrar-comentario").attr("href", recipeId + '/comentarios/' + commentId + '/borrar');
	 });
 }
 
 /*******************************************************
  * NEW RECIPE. Add ingredients and steps
  *******************************************************/

 function addIngredient() {
 	let elements = parseInt(document.getElementById('firstIngredients').childElementCount) +
 	parseInt(document.getElementById('secondIngredients').childElementCount);

 	let idx = elements + 1;

 	let ingredientForm = `
 	<li id="ingredient_form_` + idx + `" class="collection-item avatar">
 	<i class="material-icons circle">restaurant_menu</i>
 	<span class="title">
 	<div class="input-field">
 	<input placeholder="Nombre del ingrediente" id="ingredient_` + idx + `" type="text" class="validate">
 	<label for="ingredient_` + idx + `">Ingrediente</label>
 	</div>
 	</span>
 	<div class="input-field">
 	<input placeholder="Peso en gramos" id="ingredient_weight_` + idx + `" type="text" class="validate">
 	<label for="ingredient_weight_` + idx + `">Peso en gramos</label>
 	</div>
 	<a href="#!" onclick="removeIngredient(` + idx + `)" class="tooltipped secondary-content" data-position="bottom" data-tooltip="Eliminar ingrediente"><i class="material-icons">delete</i></a>
 	</li>`;

 	if (Math.abs(elements % 2) == 1) {
 		document.getElementById('secondIngredients').innerHTML += ingredientForm;
 		M.updateTextFields();
 	} else {
 		document.getElementById('firstIngredients').innerHTML += ingredientForm;
 		M.updateTextFields();
 	}
 }

 function removeIngredient(ingredient) {
 	if (Math.abs(ingredient % 2) == 1) {
 		document.getElementById('firstIngredients').removeChild(document.getElementById('ingredient_form_' + ingredient));
 	} else {
 		document.getElementById('secondIngredients').removeChild(document.getElementById('ingredient_form_' + ingredient));
 	}
 }

 function addStep() {
 	let steps = document.getElementById('stepsForm');
 	let idx = parseInt(steps.childElementCount) + 1;

 	let stepsForm = `
 	<li id="step_form_` + idx + `" class="collection-item avatar">
 	<i class="material-icons circle">done</i>

 	<div class="input-field col s12">
 	<textarea id="step_` + idx + `" class="materialize-textarea"></textarea>
 	<label for="step_` + idx + `">Paso #` + idx + `</label>
 	</div>
 	<a href="#!" onclick="removeStep(` + idx + `)" class="tooltipped secondary-content" data-position="bottom" data-tooltip="Eliminar paso"><i class="material-icons">delete</i></a>
 	</li>`;

 	steps.innerHTML += stepsForm;

 }

 function removeStep(step) {
 	document.getElementById('stepsForm').removeChild(document.getElementById('step_form_' + step));
 }
 
 
 /*******************************************************
  * AJAX API CALLS (/api)
  *******************************************************/
 
  function getFavouriteTags() {
 	let instance = M.Chips.getInstance($(".chips-tags"));
 	
 	fetch('/api/users/' + gastronomo.userId + '/tags')
 	.then(res => res.json())
 	.then(data => {
 		$.each(data, function(key, t) {
 			instance.addChip({tag: t.tag})
 		})
 	});	
 }

 function addFavTagListener() {
 	const headers = {
 		"Content-Type": "application/json",				
 		"X-CSRF-TOKEN": gastronomo.csrf.value
 	};
 	
 	let button = $("#add-favourite-tag").click(() => {
 		let chips = M.Chips.getInstance($(".chips-tags")).chipsData;
 		
 		chips.forEach(function(t) {
 			fetch('/api/users/' + gastronomo.userId + '/tags', {
 				headers: headers,
 				method: 'POST',
 				body: JSON.stringify({ tag: t.tag })
 			}).then(function(response) {
 				console.log(response);
 			});
 		});
 		
 		M.toast({html: '¡Tags añadidos a favoritos!'});
 	});
 }
 
 function getFavouriteIngredients() {
	 	let instance = M.Chips.getInstance($(".chips-ingredientes"));
	 	
	 	fetch('/api/users/' + gastronomo.userId + '/ingredients')
	 	.then(res => res.json())
	 	.then(data => {
	 		$.each(data, function(key, t) {
	 			instance.addChip({tag: t.customName})
	 		})
	 	});	
	 }
 
 function addFavIngredientListener() {
	 	const headers = {
	 		"Content-Type": "application/json",				
	 		"X-CSRF-TOKEN": gastronomo.csrf.value
	 	};
	 	
	 	let button = $("#add-favourite-ingredient").click(() => {
	 		let chips = M.Chips.getInstance($(".chips-ingredientes")).chipsData;
	 		
	 		chips.forEach(function(t) {
	 			fetch('/api/users/' + gastronomo.userId + '/ingredients', {
	 				headers: headers,
	 				method: 'POST',
	 				body: JSON.stringify({ customName: t.tag })
	 			}).then(function(response) {
	 				console.log(response);
	 			});
	 		});
	 		
	 		M.toast({html: '¡Ingredientes añadidos a favoritos!'});
	 	});
	 }


 function removeFavTag(element, chip) {
 	let tag = chip.innerText.split('close')[0];
 	
 	const headers = {
 		"Content-Type": "application/json",				
 		"X-CSRF-TOKEN": gastronomo.csrf.value
 	};
 	
 	fetch('/api/users/' + gastronomo.userId + '/tags', {
 		headers: headers,
 		method: 'DELETE',
 		body: JSON.stringify({ tag: tag })
 	}).then(function(response) {
 		console.log(response);
 	});
 	
 	M.toast({html: 'Tag eliminado de favoritos'});
 }
 
 function removeFavIngredient(element, chip) {
 	let ingredient = chip.innerText.split('close')[0];
 	console.log(ingredient);
 	
 	const headers = {
 		"Content-Type": "application/json",				
 		"X-CSRF-TOKEN": gastronomo.csrf.value
 	};
 	
 	fetch('/api/users/' + gastronomo.userId + '/ingredients', {
 		headers: headers,
 		method: 'DELETE',
 		body: JSON.stringify({ customName: ingredient })
 	}).then(function(response) {
 		console.log(response);
 	});
 	
 	M.toast({html: 'Ingrediente eliminado de favoritos'});
 }

 /*******************************************************
  * OTHER STUFF
  *******************************************************/
 
//Red chip color instead of blue
 function changeColor() {
 	document.querySelectorAll('.buscador .chip').forEach(chip => {
 		chip.classList.add('red');
 	})
 }
