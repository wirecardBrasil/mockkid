(function() {
  const attachModalOpenEvent = () => {
    const SERVICES_SECTION = document.querySelectorAll('.section-services');

    SERVICES_SECTION.forEach(section => {
      section.addEventListener('click', () => {
        const DIALOG = section.querySelector('.mdl-dialog')

        if (!DIALOG.hasAttribute('open')) {
          DIALOG.showModal();
        }
      });
    });
  }

  const deattachModalOpenEvent = () => {
    const MODAL_CLOSE_BUTTONS = document.querySelectorAll('.mdl-button-dialog.close');
    
    MODAL_CLOSE_BUTTONS.forEach(button => {
      button.addEventListener('click', e => {
        e.stopPropagation()
        button.parentNode.parentNode.close();
      });
    });
  }

  const controllAccordions = () => {
    const ACCORDIONS = document.querySelectorAll('.accordion-link');

    ACCORDIONS.forEach(accordion => {
      accordion.addEventListener('click', e => {
        e.stopPropagation();
        accordion.nextElementSibling.classList.toggle('is-opened');
      })
    })
  }

  attachModalOpenEvent();
  deattachModalOpenEvent();
  controllAccordions();
})();