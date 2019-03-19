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
        'Alto en fibra': null,
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


document.addEventListener('DOMContentLoaded', function() {
	addIndexFiltersListeners();	
	
	let elems = document.querySelectorAll('.chips');
	let instances = M.Chips.init(elems, { onChipAdd: changeColor });

	elems = document.querySelectorAll('.chips-ingredientes');
	instances = M.Chips.init(elems, {
        placeholder: '+Ingrediente',
        secondaryPlaceholder: '+Ingrediente',
    });

	elems = document.querySelectorAll('.chips-alergias');
	instances = M.Chips.init(elems, {
        placeholder: '+Alergia',
        secondaryPlaceholder: '+Alergia',
    });

});

// Color rojo en vez del azul por defecto
function changeColor() {
    document.querySelectorAll('.buscador .chip').forEach(chip => {
        chip.classList.add('red');
    })
}

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

function loadIngredients() {
	let ingredients = $(".ingredientes")
	let instance = M.Chips.getInstance($(".chips"));
	
	
	$.each(ingredients, function(key, ingredient) {
		instance.addChip({
		    tag: ingredient.textContent.trim()
		  });
	});
}

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