import * as React from 'react';
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import CssBaseline from '@mui/material/CssBaseline';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { NavLink, useNavigate } from 'react-router-dom';
import CONSTANTS from '../utils/Constants';
import { AppBar, Button, InputBase, Menu, MenuItem, Avatar, Collapse, ThemeProvider, createTheme } from '@mui/material';
import Switch from '@mui/material/Switch';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';
import SearchIcon from '@mui/icons-material/Search';
import { useAuth } from '../../features/AuthProvider ';
import { AssistWalker, ExpandLess, ExpandMore } from '@mui/icons-material';
import useCommonEffect from '../Session/useCommonEffect';
import api from '../utils/axiosSetup';
import axios from 'axios';

const drawerWidth = 240;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })(({ theme, open }) => ({
  flexGrow: 1,
  padding: theme.spacing(3),
  transition: theme.transitions.create('margin', {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  marginLeft: `-${drawerWidth}px`,
  ...(open && {
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
    marginLeft: 0,
  }),
}));


const CustomAppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
  transition: theme.transitions.create(['margin', 'width'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    marginLeft: `${drawerWidth}px`,
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

const RecursiveList = ({ modules }) => {
  const navigate = useNavigate();
  const [openItems, setOpenItems] = React.useState({});

  const handleToggle = (id) => {
    setOpenItems((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  return (
    <List>
      {modules.map((item) => {
        const hasChildren = item.subModule?.length > 0;

        return (
          <div key={item.subModuleId}>
            <ListItem disablePadding>
              <ListItemButton onClick={() => (hasChildren ? handleToggle(item.subModuleId) : navigate('/' + item.requestMapping))}>
                <ListItemIcon>
                  <i className={item.icon}></i>
                </ListItemIcon>
                <ListItemText primary={item.subModuleName} />
                {hasChildren ? (openItems[item.subModuleId] ? <ExpandLess /> : <ExpandMore />) : null}
              </ListItemButton>
            </ListItem>

            {/* Display nested submodules only when expanded */}
            {hasChildren && (
              <Collapse in={openItems[item.subModuleId]} timeout="auto" unmountOnExit>
                <List sx={{ pl: 4 }}>
                  <RecursiveList modules={item.subModule} />
                </List>
              </Collapse>
            )}
          </div>
        );
      })}
    </List>
  );
};

// Example Usage
const MyComponent = ({ data }) => {
  return <RecursiveList modules={data} />;
};

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  ...theme.mixins.toolbar,
  justifyContent: 'space-between',
}));

export default function PersistentDrawerLeft() {
  const [countries, setCountries] = React.useState([]);
  const fetchCountries = async () => {
    const response = await api.get(CONSTANTS.BASE_URL + "/api-data/country/fetch-all-countries")
    var resultData = response.data;
    console.log("countries:" + JSON.stringify(resultData));
    setCountries(resultData);
  }
  const theme = useTheme();
  const [open, setOpen] = React.useState(false);
  const [data, setData] = React.useState([]);
  const history = useNavigate();
  const { authToken, logout } = useAuth();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const openMenu = Boolean(anchorEl);
  const [anchorElCountry, setAnchorElCountry] = React.useState(null);
  const openMenuCountry = Boolean(anchorElCountry);

  const drawerRef = React.useRef(null);  // Create a ref for the drawer
  const mainRef = React.useRef(null);    // Create a ref for the main content area
  const fetchLoggedInUsersAccess = async () => {
    if (data.length > 0) {
      return; // Prevents unnecessary calls if data is already populated
    }

    const token = localStorage.getItem('authToken');
    if (!token) {
      console.error('No token found in localStorage');
      return;
    }

    try {
      const response = await api.post(
        CONSTANTS.BASE_URL + '/api-data/auth/get-user-access',
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response.data);
      setData(response.data);
    } catch (error) {
      console.error('Error fetching user access:', error);
    }
  };

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };

  const handleLogout = () => {
    logout();
    history('/');
  };

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleCountryMenuOpen = (event) => {
    setAnchorElCountry(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };
  const handleCountryMenuClose = () => {
    setAnchorElCountry(null);
  };

  // Close the drawer when clicking outside of the drawer
  React.useEffect(() => {
    fetchCountries();
    fetchLoggedInUsersAccess();
    const handleClickOutside = (event) => {
      if (drawerRef.current && !drawerRef.current.contains(event.target) && mainRef.current && !mainRef.current.contains(event.target)) {
        setOpen(false); // Close the drawer when clicking outside
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);
  function goToHome() {
    history("/ecommerce/home")
  }
  function setCountryCode(countryObj) {
    localStorage.setItem('currencyCode', countryObj.currencyCode);
    console.log("currency code:" + localStorage.getItem('currencyCode'));
  }
  const [selectedItem, setSelectedItem] = React.useState(localStorage.getItem('currencyCode')); // Track selected item

  const handleSelect = (item) => {
    localStorage.setItem('currencyCode', item);
    setSelectedItem(item); // Update selected item
    handleCountryMenuClose();
    window.location.reload();
  };
  const [darkMode, setDarkMode] = React.useState(
    localStorage.getItem('theme') === 'dark'
  );
  const lightTheme = createTheme({
    palette: {
      mode: 'light',
    },
  });

  const darkTheme = createTheme({
    palette: {
      mode: 'dark',
    },
  });
  function fetchUsersCartCount() {
    axios.get(CONSTANTS.BASE_URL + "/customer/fetch-users-cart-count", {
      headers: {
        "Authorization": "Bearer " + localStorage.getItem('authToken'),
      }
    })
      .then((result) => {
        console.log("user cart count is :" + JSON.stringify(result));
        localStorage.setItem('cartItemNo', result.data);
      })
  }

  // Save theme preference in localStorage
  React.useEffect(() => {
    localStorage.setItem('theme', darkMode ? 'dark' : 'light');
    fetchUsersCartCount();
  }, [darkMode]);

  return (
    <ThemeProvider theme={darkMode ? darkTheme : lightTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <CustomAppBar position="fixed" open={open}>
          <Toolbar>
            <IconButton
              color="inherit"
              aria-label="open drawer"
              onClick={handleDrawerOpen}
              edge="start"
              sx={{ mr: 2, ...(open && { display: 'none' }) }}
            >
              <MenuIcon />
            </IconButton>
            <Typography variant="h6" noWrap sx={{ flexGrow: 1 }}>
              <span onClick={goToHome}> E-Commerce</span>
            </Typography>
            {/* Dark Mode Toggle */}
            <IconButton onClick={() => setDarkMode(!darkMode)} color="inherit">
              {darkMode ? <Brightness7Icon /> : <Brightness4Icon />}
            </IconButton>

            <Box sx={{ display: 'flex', alignItems: 'center', backgroundColor: 'white', borderRadius: 1, padding: '0 8px' }}>
              <SearchIcon />
              <InputBase placeholder="Search…" sx={{ marginLeft: 1 }} />
            </Box>
            <IconButton onClick={() => history('/ecommerce/cart/view')}>
              <i className="fa badge fa-lg" value={localStorage.getItem('cartItemNo')}>&#xf07a;</i>
            </IconButton>

            <IconButton onClick={handleCountryMenuOpen} sx={{ marginLeft: 2 }}>
              <Avatar src={require('../../assets/images/countries.png')} alt="Profile" />
            </IconButton>
            <Menu anchorEl={anchorElCountry} open={openMenuCountry} onClose={handleCountryMenuClose}>
              {countries.map((country) => (
                <MenuItem selected={selectedItem === country.currencyCode} onClick={() => handleSelect(country.currencyCode)}>{country.countryFlag} {country.countryName}</MenuItem>
              ))}
            </Menu>
            <IconButton onClick={handleMenuOpen} >
              <Avatar src={require('../../assets/images/user.png')} alt="Profile" />
            </IconButton>
            <Menu anchorEl={anchorEl} open={openMenu} onClose={handleMenuClose}>
              <MenuItem onClick={() => history('/ecommerce/profile')}>My Profile</MenuItem>
              <MenuItem onClick={() => history('/settings')}>Settings</MenuItem>
              <Divider />
              <MenuItem onClick={handleLogout}>Log out</MenuItem>
            </Menu>

          </Toolbar>
        </CustomAppBar>
        <Drawer
          ref={drawerRef}  // Attach the ref to the drawer component
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            '& .MuiDrawer-paper': {
              width: drawerWidth,
              boxSizing: 'border-box',
            },
          }}
          variant="persistent"
          anchor="left"
          open={open}
        >
          <DrawerHeader>
            <IconButton onClick={handleDrawerClose}>
              {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
            </IconButton>
          </DrawerHeader>
          <Divider />
          <MyComponent data={data} />
        </Drawer>
        <Main ref={mainRef} open={open}>
          <DrawerHeader />
        </Main>
      </Box >
    </ThemeProvider >
  );
}
