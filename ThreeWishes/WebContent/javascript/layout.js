function pushsubmit( option ) {
  document.input.sendmywishes.value = option;
  document.input.submit() ;
}

function select (_tab, _view) {
  if (window.current) {
    window.current.tab.className = 'tab deselected';
    window.current.view.style.visibility = 'hidden';
  } else
    window.current = {};
  	(window.current.tab = _tab).className = 'tab selected';
  	(window.current.view = document.getElementById(_view)).style.visibility = 'visible';
}

window['select'] = select;

onload = function () {
	
  function loaded () {
    document.getElementById('default').onclick();
  }
};