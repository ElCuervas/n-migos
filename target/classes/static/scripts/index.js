// -------------------------- INICIO Y REGISTRO -------------------------- //
document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', async () => {
            try {
                const response = await fetch('/auth/logout', {
                    method: 'POST',
                    credentials: 'include',
                });
                if (response.ok) {
                    alert('Sesión cerrada correctamente');
                    window.location.href = '/';
                } else {
                    alert('Error al cerrar sesión');
                }
            } catch (error) {
                console.error('Error:', error);
                alert('No se pudo cerrar la sesión');
            }
        });
    }
});

// -------------------------- BUSQUEDA DE JUEGOS -------------------------- //


async function buscarJuego(event) {
    event.preventDefault();

    const busqueda = document.getElementById('busqueda').value.trim().toLowerCase();

    if (busqueda === "") {
        aplicarFiltros(1);
        return;
    }

    const juegoLista = document.getElementById('juego-lista');

    try {
        const response = await fetch(`/buscar?titulo=${encodeURIComponent(busqueda)}`);
        if (response.ok) {
            const juegosFiltrados = await response.json();
            mostrarJuegosFiltrados(juegosFiltrados);
        } else {
            console.error("Error al buscar juegos:", response.statusText);
        }
    } catch (error) {
        console.error("Error al buscar juegos:", error);
    }
}


function mostrarJuegosFiltrados(juegosFiltrados) {
    const juegoLista = document.querySelector('.juego-lista');
    juegoLista.innerHTML = '';

    if (juegosFiltrados.length === 0) {
        juegoLista.innerHTML = '<p>No se encontraron juegos compatibles con los filtros aplicados.</p>';
        return;
    }

    juegosFiltrados.forEach(juego => {
        const juegoItem = document.createElement('div');
        juegoItem.classList.add('juego-item');
        juegoItem.innerHTML = `
            <img src="${juego.imagen}" alt="${juego.titulo}">
            <h3>${juego.titulo}</h3>
            <p>Calificación: ${juego.calificacion}</p>
            <a href="/info?id=${juego.id}" class="btn-jugar">+info</a>
        `;
        juegoLista.appendChild(juegoItem);
    });
}

// -------------------------- FILTRO POR GENEROS -------------------------- //


let generosSeleccionados = [];

document.getElementById('filtro-genero').addEventListener('click', async () => {
    const popupGeneros = document.getElementById('popup-generos');
    const listaGeneros = document.getElementById('lista-generos');

    try {
        const response = await fetch('/generos');
        if (response.ok) {
            const generos = await response.json();
            listaGeneros.innerHTML = '';
            generos.forEach(genero => {
                const li = document.createElement('li');
                li.textContent = genero.nombre;
                li.style.cursor = 'pointer';
                li.addEventListener('click', () => {
                    if (generosSeleccionados.includes(genero.nombre)) {
                        generosSeleccionados = generosSeleccionados.filter(g => g !== genero.nombre);
                        li.style.backgroundColor = '';
                    } else {
                        generosSeleccionados.push(genero.nombre);
                        li.style.backgroundColor = '#444';
                    }
                    actualizarFiltrosAplicados();
                });
                listaGeneros.appendChild(li);
            });
        } else {
            console.error("Error al cargar los géneros:", response.statusText);
        }
    } catch (error) {
        console.error("Error al cargar los géneros:", error);
    }

    popupGeneros.style.display = 'flex';
});

document.getElementById('cerrarPopup').addEventListener('click', () => {
    const popupGeneros = document.getElementById('popup-generos');
    popupGeneros.style.display = 'none';
    aplicarFiltros();
});

// -------------------------- FILTRO POR PUNTUACION -------------------------- //
let puntuacionSeleccionada = null;
let filtroTipo = null;

document.getElementById('filtro-puntuacion').addEventListener('click', () => {
    const popupPuntuacion = document.getElementById('popup-puntuacion');
    popupPuntuacion.style.display = 'flex';

    // Restablecer estado al abrir el popup
    filtroTipo = null;
    puntuacionSeleccionada = null;
    document.querySelectorAll('.estrella').forEach(e => e.classList.remove('selected'));
    actualizarEstadoBotones(null);
});

document.getElementById('filtro-mayor-que').addEventListener('click', () => {
    filtroTipo = 'mayor';
    actualizarEstadoBotones('filtro-mayor-que');
    console.log("Filtro seleccionado: Mayor que");
});

document.getElementById('filtro-menor-que').addEventListener('click', () => {
    filtroTipo = 'menor';
    actualizarEstadoBotones('filtro-menor-que');
    console.log("Filtro seleccionado: Menor que");
});

document.querySelectorAll('.estrella').forEach((estrella, index, estrellas) => {
    estrella.addEventListener('click', () => {
        puntuacionSeleccionada = estrella.dataset.puntuacion;
        console.log("Puntuación seleccionada:", puntuacionSeleccionada);
        estrellas.forEach((e, i) => {
            if (i <= index) {
                e.classList.add('selected');
            } else {
                e.classList.remove('selected');
            }
        });
    });
});

// Función para actualizar el estado de selección de los botones
function actualizarEstadoBotones(idSeleccionado) {
    const botones = ['filtro-mayor-que', 'filtro-menor-que'];
    botones.forEach(id => {
        const boton = document.getElementById(id);
        if (id === idSeleccionado) {
            boton.classList.add('boton-seleccionado');
        } else {
            boton.classList.remove('boton-seleccionado');
        }
    });
}

// Aplicar filtro de puntuación
document.getElementById('cerrarPopupPuntuacion').addEventListener('click', () => {

    console.log(filtroTipo)
    if (filtroTipo==null) {
        alert("Por favor selecciona una condicional (Mayor que o Menor que).");
        return;
    }
    if (!puntuacionSeleccionada) {
        alert("Por favor selecciona una puntuación.");
        return;
    }
    const popupPuntuacion = document.getElementById('popup-puntuacion');
    popupPuntuacion.style.display = 'none';

    aplicarFiltros();
});


// -------------------------- APLICACION DE FILTROS -------------------------- //


async function aplicarFiltros(pagina = 1) {
    const generos = generosSeleccionados.join(',');
    const tipoFiltro = filtroTipo;
    const puntuacion = puntuacionSeleccionada;

    // Construir la URL con los parámetros existentes
    let url = `/filtrar?pagina=${pagina}`;
    if (generos) {
        url += `&generos=${encodeURIComponent(generos)}`;
    }
    if (tipoFiltro && puntuacion) {
        url += `&tipo=${tipoFiltro}&puntuacion=${puntuacion}`;
    }

    try {
        console.log("URL generada para filtros:", url);
        const response = await fetch(url);
        if (response.ok) {
            const data = await response.json();
            mostrarJuegosFiltrados(data.juegos);
            mostrarPaginacion(data.totalPaginas, data.paginaActual);
            actualizarFiltrosAplicados();
        }
    } catch (error) {
        console.error("Error al aplicar filtros combinados:", error);
    }
}


function actualizarFiltrosAplicados() {
    const contenedorFiltros = document.getElementById('lista-filtros');
    const botonBorrarFiltros = document.getElementById('borrar-filtros');

    let filtros = [...generosSeleccionados];

    if (filtroTipo && puntuacionSeleccionada) {
        const simbolo = filtroTipo === 'mayor' ? '>' : '<';
        filtros.push(`${simbolo}${puntuacionSeleccionada}★`);
    }

    if (filtros.length > 0) {
        contenedorFiltros.textContent = filtros.join(', ');
        botonBorrarFiltros.style.display = 'inline-block';
    } else {
        contenedorFiltros.textContent = 'Ninguno';
        botonBorrarFiltros.style.display = 'none';
    }
}

document.getElementById('borrar-filtros').addEventListener('click', async () => {
    generosSeleccionados = [];
    puntuacionSeleccionada = null;
    filtroTipo = null;

    document.querySelectorAll('.estrella').forEach(e => e.classList.remove('selected'));
    actualizarFiltrosAplicados();

    try {
        const response = await fetch('/juegos?pagina=1');
        if (response.ok) {
            const data = await response.json();
            mostrarJuegosFiltrados(data.juegos);
            mostrarPaginacion(data.totalPaginas, data.paginaActual);
        } else {
            console.error("Error al cargar todos los juegos:", response.statusText);
        }
    } catch (error) {
        console.error("Error al cargar todos los juegos:", error);
    }
});


// -------------------------- PAGINACION -------------------------- //


function mostrarPaginacion(totalPaginas, paginaActual) {
    const paginacionContainer = document.getElementById('pagination');
    paginacionContainer.innerHTML = ''; // Limpiar paginación previa

    if (totalPaginas > 1) {
        if (paginaActual > 1) {
            const botonAnterior = document.createElement('button');
            botonAnterior.textContent = 'Anterior';
            botonAnterior.onclick = () => aplicarFiltros(paginaActual - 1);
            paginacionContainer.appendChild(botonAnterior);
        }

        for (let i = 1; i <= totalPaginas; i++) {
            const boton = document.createElement('button');
            boton.textContent = i;
            boton.className = i === paginaActual ? 'active-page' : '';
            boton.onclick = () => aplicarFiltros(i);
            paginacionContainer.appendChild(boton);
        }

        if (paginaActual < totalPaginas) {
            const botonSiguiente = document.createElement('button');
            botonSiguiente.textContent = 'Siguiente';
            botonSiguiente.onclick = () => aplicarFiltros(paginaActual + 1);
            paginacionContainer.appendChild(botonSiguiente);
        }
    }
}

// -------------------------- AÑADIR A BIBLIOTECA -------------------------- //
function addToLibrary(button) {
    const gameId = button.getAttribute('data-id');

    fetch(`/biblioteca/${gameId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                alert("El juego ha sido agregado a tu biblioteca!");
            } else {
                alert("Ya tienes este juego en biblioteca!!");
            }
        })
        .catch(error => {
            console.error("Error al agregar el juego a la biblioteca:", error);
        });
}

function toggleMenu() {
    const submenu = document.querySelector('.submenu-usuario');
    submenu.classList.toggle('mostrar');
}
