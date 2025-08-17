import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  components: {
    // Override Button component
    MuiButton: {
      styleOverrides: {
        // Apply to all button variants
        root: {
          // Add subtle border for all buttons
          border: '1px solid rgba(0, 0, 0, 0.23)',
          '&:hover': {
            border: '1px solid rgba(0, 0, 0, 0.5)',
          },
        },
        // Override for text variant (default)
        text: {
          backgroundColor: 'transparent',
          border: '1px solid rgba(0, 0, 0, 0.23)',
          color: 'rgba(0, 0, 0, 0.87)',
          '&:hover': {
            backgroundColor: 'rgba(0, 0, 0, 0.04)',
            border: '1px solid rgba(0, 0, 0, 0.5)',
          },
        },
        // Keep outlined variant as is (already has border)
        outlined: {
          // Keep default outlined button styling
        },
        // Keep contained variant as is (already visible)
        contained: {
          // Keep default contained button styling
        },
      },
    },
  },
  // You can also override the default props
  // components: {
  //   MuiButton: {
  //     defaultProps: {
  //       variant: 'outlined', // Make outlined the default variant
  //     },
  //   },
  // },
});