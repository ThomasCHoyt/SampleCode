library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity slow_clk_signal is
port(clk_in     :in std_logic;
	 clk_out    :out std_logic;
	 reset      :in std_logic;
	 clockV     :out std_logic;
	 led1       :out std_logic;
	 led2       :out std_logic;
	 led3       :out std_logic;
	 led4       :out std_logic;
	 led5       :out std_logic;
	 led6       :out std_logic;
	 led7       :out std_logic;
	 led8       :out std_logic;
	 led9       :out std_logic;
	 led10       :out std_logic;
	 led11       :out std_logic
	);
end slow_clk_signal;

architecture slow_clock of slow_clk_signal is

signal counter   :std_logic_vector(24 downto 0);
signal int_clk   :std_logic;
signal term_cnt  :std_logic;


begin

clockV <= '1';
led1   <= '1';
led2   <= '1';
led3   <= '1';
led4   <= '1';
led5   <= '1';
led6   <= '1';
led7   <= '1';
led8   <= '1';
led9   <= '1';
led10  <= '1';
led11  <= '1';

-- process is a counter that counts each clk_in period counter restarts 
-- at 32 Million cycles or one second
	process(clk_in, reset)
	begin
		
		if(reset = '1')then
			counter <= (others => '0');
			
		elsif(clk_in'event AND clk_in = '1')then
			if(term_cnt = '1')then
				counter <= (others => '0');
			
			elsif(term_cnt = '0')then
				counter <= counter + 1;
			
			else
				counter <= counter;
			
			end if;

		end if;	
		
	end process;
	
	-- this logic inverts term_count every sec. with a 32MHz clk_in frequency
	term_cnt <= '1' when(counter = "1111010000100100000000000")else '0';


	process(clk_in,reset,term_cnt)
	begin
		
		if(reset = '1')then
			int_clk <= '1';
	
		elsif(clk_in'event and clk_in = '1')then

			if(term_cnt = '1')then
				int_clk <= not int_clk; 
			
			end if;
		end if;	
	end process;

	clk_out <= int_clk;
	
end slow_clock;