import React, { useEffect } from "react";
import useStateRef from "react-usestateref";


const DashBord = () => {
  const [examsList, setExamsList, refExamsList] = useStateRef("");
  const [usersDetail, setUsersDetail, refUsersDetail] = useStateRef("");
  var queryParam = window.location.search;

  const queryParamKeypairs = new URLSearchParams(queryParam);

  var partyIdOfAdmin = queryParamKeypairs.get("partyIdOfAdmin");
  

  useEffect(() => {
    getExamsList();
   
  }, []);

  function getExamsList() {
    fetch(
      "https://localhost:8443/onlineexamapplication/control/get-exam-or-exam-list",
      { credentials: "include" }
    )
      .then((res) => res.json())
      .then((data) => {
        setExamsList(data.Exam_List);
      })
      .catch((error) => console.log(error));
  }

  function getUserList() {
    fetch("https://localhost:8443/onlineexamapplication/control/get-users", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({ partyIdOfAdmin: partyIdOfAdmin }),
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setUsersDetail(data.ListOfUserDetails.ListOfUsers);
      });
  }

  return (
    <>
      <div className="row mt-3 ">
        {refExamsList.current &&
          refExamsList.current.map((exam) => {
            return (
              <>
                <div class="border col-sm-3 card p-0 m-5 offset-md-1 custom-bd-color">
                  <div class="card-header bg-dark text-white  fs-1 fw-bolder ">
                    <h5 className="bi bi-motherboard">Exam</h5>
                  </div>
                  <div className="card-body body-hover">
                    <h5 className="bi bi-journals px-2">{exam.examName}</h5>
                  </div>
                </div>
              </>
            );
          })}
      </div>

      <div className="row">
        {refUsersDetail.current &&
          refUsersDetail.current.map((user) => {
            return (
              <>
                <div class="border col-sm-3 card p-0 m-5 offset-md-1 custom-bd-color">
                  <div class="card-header  bg-dark text-white  fs-1 fw-bolder">
                    <h5 className="bi bi-person-square">User</h5>
                  </div>
                  <div className="card-body dashbord-card-body body-hover ">
                    <h5 className="bi bi-person px-2">
                      {user.firstName} {user.lastName}
                    </h5>
                  </div>
                </div>
              </>
            );
          })}
      </div>
    </>
  );
};

export default DashBord;
