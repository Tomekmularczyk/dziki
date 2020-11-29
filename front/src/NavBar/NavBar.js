import styled from "styled-components";
import regionDropdown from "./region-dropdown.png";
import downloadRaport from "./download-raport.png";
import statsChange from "./stats-change.png";
import hamburger from "./hamburger.png";
import stats from "./stats.png";

const Nav = styled.nav`
  display: flex;
  width: 100%;
  padding: 1rem 2rem 2rem 2rem;
`;

const RegionDropdown = styled.img.attrs({ src: regionDropdown })`
  margin-top: 1rem;
  width: 200px;
`;

const Hamburger = styled.img.attrs({ src: hamburger })`
  margin-top: 3px;
  margin-right: 30px;
  width: 35px;
  cursor: pointer;
`;

const DownloadRaportButton = styled.img.attrs({ src: downloadRaport })`
  width: 400px;
  margin-top: 1rem;
  cursor: pointer;
`;

const StatsChange = styled.img.attrs({ src: statsChange })`
  width: 470px;
  margin-top: 1rem;
  cursor: pointer;
`;

const Stats = styled.img.attrs({ src: stats })`
  width: 250px;
  height: 128px;
  margin-top: 16px;
  margin-right: 90px;
  cursor: pointer;
`;

const H2 = styled.h2`
  font-size: 2rem;
  display: flex;
  align-items: center;
`;

const Container = styled.div`
  flex-grow: 1;
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-end;
`;

export function NavBar() {
  return (
    <Nav>
      <div>
        <H2>
          <Hamburger />
          Monitoring ASF
        </H2>
        <RegionDropdown />
        <a href="/raport-przypadkow.xlsx" download>
          <DownloadRaportButton />
        </a>
      </div>
      <Container>
        <Stats />
        <StatsChange />
      </Container>
    </Nav>
  );
}
