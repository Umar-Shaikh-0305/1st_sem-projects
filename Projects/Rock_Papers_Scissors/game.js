let userScore=0;
let compScore=0;

const choices = document.querySelectorAll(".choice");
const msg = document.querySelector("#msg")
let userscoreboard = document.querySelector(".userscore")
let compscoreboard = document.querySelector(".compscore")

const compchoice =()=>{
    const option = ["rock","paper","scissors"];
    const randindx=Math.floor(Math.random()*3);
     return option[randindx];
}

const Gamedraw = () => {
   msg.innerText="Game Was Drawn, Play Again.";
   msg.style.backgroundColor="#081b31";
};

const showwinner=(userwin,userchoice,computerchoice)=>{
    if(userwin){
        userScore++;
        userscoreboard.innerText = userScore
        msg.innerText=`You Win! Your ${userchoice} beats ${computerchoice}`;
        msg.style.backgroundColor="green";
    } else {
        compScore++;
        compscoreboard.innerText = compScore
         msg.innerText=`You lost! ${computerchoice} beats yours ${userchoice}`;
         msg.style.backgroundColor="red";
    }
};

const playgame =(userchoice)=>{
   console.log("userchoice =",userchoice);
   const computerchoice = compchoice();
   console.log("compchoice =",computerchoice);

   if (userchoice === computerchoice) {
    return Gamedraw();
   } else {
    let userwin=true;
    if (userchoice==="rock"){
        // scissors, paper
        userwin=computerchoice==="paper" ? false : true;
    } else if(userchoice==="paper"){
        userwin=computerchoice==="scissors" ? false : true;
    } else {
    userwin=computerchoice==="rock" ? false : true;
    };
    showwinner(userwin,userchoice,computerchoice);
};
};

choices.forEach((choice) => {
    console.log("choice")
    choice.addEventListener("click",() =>{
        const userchoice= choice.getAttribute("id")
       playgame(userchoice);
    })
});
