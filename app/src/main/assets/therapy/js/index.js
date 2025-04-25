const ham = document.getElementById("ham");
const burger = document.querySelector(".burger");
const cancel = document.querySelector(".cancel");
const menu = document.getElementById("menu");

// Initially hide the cancel button
cancel.style.display = "none";

// Toggle menu open/close state
const toggleMenu = () => {
  if (menu.classList.contains("open")) {
    menu.classList.remove("open");
    cancel.style.display = "none";
    burger.style.display = "block";
    console.log("Menu closed");
  } else {
    menu.classList.add("open");
    burger.style.display = "none";
    cancel.style.display = "block";
    console.log("Menu opened");
  }
};

// Accordion functionality
const accItems = document.querySelectorAll(".accordion__item");

// Add a click event for all accordion items
accItems.forEach((acc) => acc.addEventListener("click", toggleAcc));

function toggleAcc() {
  // Remove active class from all items except the current one (this)
  accItems.forEach((item) => item !== this ? item.classList.remove("accordion__item--active") : null);

  // Toggle the active class on the current item
  this.classList.toggle("accordion__item--active");
}

// Back-to-top functionality
const backTopElement = document.getElementById("back-top-div");
const minScrolledAmountToBackTop = 800;

window.addEventListener("scroll", () => {
  if (window.scrollY < minScrolledAmountToBackTop) {
    backTopElement.classList.add("hidden");
  } else {
    backTopElement.classList.remove("hidden");
  }
});

const backTop = () => {
  window.scrollTo(0, 0);
};

backTopElement.addEventListener("click", backTop);

// Add click event to the hamburger icon to toggle the menu
ham.addEventListener("click", toggleMenu);

// Close the menu when a link inside it is clicked
document.querySelectorAll('.menu a').forEach(link => {
  link.addEventListener('click', toggleMenu);
});

// Accordion button functionality
document.addEventListener("DOMContentLoaded", function() {
  const accordionButtons = document.querySelectorAll('.accordion__btn');

  accordionButtons.forEach(button => {
    button.addEventListener('click', () => {
      // Get the content element
      const content = button.nextElementSibling;

      // Toggle the content visibility using a class
      if (content.style.display === "active");

      // Toggle the icon (plus/minus)
      const icon = button.querySelector('.accordion__icon i');
      icon.classList.toggle('fa-plus');
      icon.classList.toggle('fa-minus');
    });
  });
});
