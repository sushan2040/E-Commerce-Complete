const tableCustomStyles = {
  table: {
    style: {
      border: '1px solid #ddd', // Adds a border around the table
      borderRadius: '8px',      // Optional: Rounded corners for a smoother look
    },
  },
  headCells: {
    style: {
      fontWeight: 'bold',
      justifyContent: 'center',
      background: 'linear-gradient(180deg, #7BD5F5 0%, #787FF6 137.5%)',
      color: 'white',
      fontSize: '12px',
      borderBottom: '1px solid #ddd', // Adds a bottom border to header cells
    },
  },
  rows: {
    style: {
      '&:hover': {
        backgroundColor: '#f1f1f1', // Hover effect for rows
      },
      fontSize: '12px',
      minHeight: '32px', // Condensed row height
      borderBottom: '1px solid #ddd', // Adds border between rows
    },
  },
  cells: {
    style: {
      justifyContent: 'center',
      padding: '8px', // Makes the table more compact
      borderRight: '1px solid #ddd', // Adds borders between columns
    },
  },
};

export default tableCustomStyles;