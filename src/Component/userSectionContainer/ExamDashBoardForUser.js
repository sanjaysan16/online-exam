import { port, protocol } from "../fetchConst";
import { useState, useEffect } from "react";
import AllottedExamsToUser from "./AllottedExamsToUser";

const ExamDashBoardForUser = () => {

  const uri = `${protocol}://${window.location.hostname}:${port}`;

  const [examList, setExamList] = useState("");
  // const [isAdmin, setIsAdmin] = useState(false);
  // const [message, setMessage] = useState("");


  useEffect(() => {
    getUserExamMappingList();
    // adminOrUserCheck();
  }, []);
  const getUserExamMappingList = async () => {
    const userExamMappedFetch = await fetch('https://localhost:8443/onlineexamapplication/control/get-list-of-user-exam-mapping', {
      credentials: 'include'
    });
    const userExamMappedList = await userExamMappedFetch.json();
    console.log(userExamMappedFetch);
    setExamList(userExamMappedList.mappedAndUnMappedExams.listOfMappedExams);
  }

  // const adminOrUserCheck = () => {
  //   fetch(`${uri}/onlineexamapplication/control/check-admin-or-user`, {
  //     credentials: "include",
  //   })
  //     .then((res) => res.json())
  //     .then((data) => {

  //       if (data.errMsg == "notAdmin") {
  //         setIsAdmin(true);
  //         setMessage(data.message);
  //         document.getElementById("examdashbordcheck").click();
  //       }
  //     })
  //     .catch((error) => console.error("Error:", error));
  // };

  const value = "home";
  return (
    <>
      <div className="pb-5">
        <div className="container-fluid py-5 text-center overflow-hidden">
          <div className="card w-100%">
            <div className="card-header ">
              <h1 style={{ float: "left" }}>Allotted Exams</h1>
            </div>

            {examList.length === 0 ? (
              <div className="p-5">
                <h1>no content available</h1>
              </div>
            ) : (
              <div className="card-body ">
                {examList && examList.map((exam, index) => {
                  return (
                    <div className="row m-3 justify-content-evenly">
                      <AllottedExamsToUser key={index} exam={exam} />
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default ExamDashBoardForUser;